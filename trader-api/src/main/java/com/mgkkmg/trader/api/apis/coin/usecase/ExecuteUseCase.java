package com.mgkkmg.trader.api.apis.coin.usecase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.ta4j.core.BarSeries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mgkkmg.globalutil.util.NumberUtils;
import com.mgkkmg.trader.api.apis.ai.dto.AiCoinResultDto;
import com.mgkkmg.trader.api.apis.ai.service.OpenAiService;
import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.api.apis.coin.enums.Decision;
import com.mgkkmg.trader.api.apis.coin.helper.IndicatorCalculator;
import com.mgkkmg.trader.api.apis.coin.helper.PerformanceCalculator;
import com.mgkkmg.trader.api.apis.coin.service.AccountInfoService;
import com.mgkkmg.trader.api.apis.coin.service.CandleService;
import com.mgkkmg.trader.api.apis.coin.service.ChartService;
import com.mgkkmg.trader.api.apis.coin.service.FearGreedIndexService;
import com.mgkkmg.trader.api.apis.coin.service.MarketPriceService;
import com.mgkkmg.trader.api.apis.coin.service.NewsService;
import com.mgkkmg.trader.api.apis.coin.service.OrderExecuteService;
import com.mgkkmg.trader.api.apis.coin.service.OrderbookService;
import com.mgkkmg.trader.api.apis.coin.service.TaService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;
import com.mgkkmg.trader.common.response.coin.OrderResponse;
import com.mgkkmg.trader.common.util.JsonUtils;
import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;
import com.mgkkmg.trader.core.domain.domains.coin.model.enums.OrderStatus;
import com.mgkkmg.trader.core.domain.domains.coin.service.TradeService;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@UseCase
public class ExecuteUseCase {

	private final AccountInfoService accountInfoService;
	private final OrderbookService orderbookService;
	private final CandleService candleService;
	private final TaService taService;
	private final FearGreedIndexService fearGreedIndexService;
	private final ChartService chartService;
	private final NewsService newsService;
	private final OpenAiService openAiService;
	private final OrderExecuteService orderExecuteService;
	private final MarketPriceService marketPriceService;
	private final TradeService tradeDomainService;

	private static final String MARKET = "KRW-BTC";
	private static final String KRW_CURRENCY = "KRW";
	private static final String BTC_CURRENCY = "BTC";

	@Value("classpath:schemas/openai_coin_result_schema.json")
	private Resource coinSchemaResource;

	@Value("classpath:templates/prompt_coin.st")
	private Resource promptCoinResource;

	@Value("classpath:templates/prompt_coin_trade_reflection.st")
	private Resource promptCoinTradeReflectionResource;

	@Value("${chart-image.path}")
	private String chartPath;

	@Value("${chart-image.upbit.file-name}")
	private String fileName;

	public void execute() {
		// 현재 투자 상태 조회
		List<AccountDto> accounts = accountInfoService.getAccounts().stream()
			.filter(account -> KRW_CURRENCY.equals(account.currency()) || BTC_CURRENCY.equals(account.currency()))
			.toList();
		String balance = JsonUtils.toJson(accounts);

		// 오더북(호가 데이터) 조회
		String orderbook = JsonUtils.toJson(orderbookService.getOrderbook(MARKET));

		// 60일 일봉 데이터 조회
		String dailyCandle = JsonUtils.toJson(candleService.getCandlesDays(MARKET));

		// 48시간 시간봉 데이터 조회
		String hourlyCandle = JsonUtils.toJson(candleService.getCandlesMinutes(MARKET));

		// 보조 지표 넣기
		BarSeries dailySeries = taService.createBarSeries(JsonUtils.fromJson(dailyCandle, new TypeReference<>() {
		}), true);
		BarSeries hourlySeries = taService.createBarSeries(JsonUtils.fromJson(hourlyCandle, new TypeReference<>() {
		}), false);

		String dailyCandleWithIndicator = IndicatorCalculator.getIndicatorsAsJson(dailySeries);
		String hourlyCandleWithIndicator = IndicatorCalculator.getIndicatorsAsJson(hourlySeries);

		// 공포 탐욕 지수
		String fearGreedIndex;
		try {
			fearGreedIndex = JsonUtils.toJson(fearGreedIndexService.getFearAndGreedIndex());
		} catch (BusinessException e) {
			fearGreedIndex = fearGreedIndexService.getUBCIFearAndGreedIndex();
		}

		// 차트 이미지
		// try {
		// 	String url = "https://upbit.com/full_chart?code=CRIX.UPBIT.KRW-BTC";
		// 	String waitForElementSelector = "#fullChartiq";
		//
		// 	chartService.captureAndSaveScreenshot(url, waitForElementSelector);
		// } catch (IOException e) {
		// 	throw new BusinessException(e.getMessage(), ErrorCode.CAPTURE_SCREENSHOT_ERROR);
		// }

		// 뉴스 제목
		String newsHeadLine = JsonUtils.toJson(newsService.getRssNewsHeadLines());
		// try {
		// 	var serpApiNewsHeadLines = newsService.getSerpApiNewsHeadLines();
		// 	if (!CollectionUtils.isEmpty(serpApiNewsHeadLines)) {
		// 		newsHeadLine = JsonUtils.toJson(serpApiNewsHeadLines);
		// 	} else {
		// 		newsHeadLine = JsonUtils.toJson(newsService.getRssNewsHeadLines());
		// 	}
		// } catch (BusinessException e) {
		// 	newsHeadLine = JsonUtils.toJson(newsService.getRssNewsHeadLines());
		// }

		// AI 호출 및 결과 받기
		String message = "Current investment status: " + balance + "\n"
			+ "Orderbook: " + orderbook + "\n"
			+ "Daily OHLCV with indicators (60 days): " + dailyCandleWithIndicator + "\n"
			+ "Hourly OHLCV with indicators (48 hours): " + hourlyCandleWithIndicator + "\n"
			+ "Fear and Greed Index: " + fearGreedIndex + "\n"
			+ "Recent news headlines: " + newsHeadLine;

		// 회고 메시지 등록
		List<TradeInfoDto> tradeInfos = tradeDomainService.getTradeInfoFromLastDays(3);

		String performance = String.format("%.2f", PerformanceCalculator.getPerformance2(tradeInfos));
		String tradeInfo = JsonUtils.toJson(
			tradeInfos.stream()
				.map(TradeInfoDto::reflectionData)
				.toList()
		);

		OpenAiChatOptions reflectionChatOptions = openAiService.getChatOptions(OpenAiApi.ChatModel.GPT_4_O_MINI);
		String reflectionPrompt = resourceToString(promptCoinTradeReflectionResource);
		String reflectionMessage = reflectionPrompt
			.replace("{trade_data}", tradeInfo)
			.replace("{current_market_data}", message)
			.replace("{performance}", performance);

		log.info("reflectionMessage: {}", reflectionMessage);

		String callReflectionContent = openAiService.callAi(reflectionMessage, reflectionChatOptions);

		// 분석 메시지 등록
		OpenAiChatOptions analyticChatOptions = openAiService.getChatOptions(OpenAiApi.ChatModel.GPT_4_O, resourceToString(coinSchemaResource));
		String analyticPrompt = resourceToString(promptCoinResource);
		String analyticMessage = analyticPrompt
			.replace("{reflection}", callReflectionContent)
			.replace("{message}", message);

		log.info("analyticMessage: {}", analyticMessage);

		// String callAnalyticContent = openAiService.callAi(analyticMessage, chartPath + "/" + fileName, analyticChatOptions);
		String callAnalyticContent = openAiService.callAi(analyticMessage, analyticChatOptions);
		AiCoinResultDto resultDto = JsonUtils.fromJson(callAnalyticContent, AiCoinResultDto.class);

		// 주문 성공 여부
		OrderStatus orderStatus = OrderStatus.SUCCESS;

		// 결과에 따른 주문
		if (Decision.BUY.getKey().equals(resultDto.decision())) {
			// 매수
			double availableBalance = getAvailableBalance();
			OrderResponse buyOrderResponse = orderExecuteService.executeBuyOrder(MARKET, availableBalance,
				resultDto.percentage());

			if (buyOrderResponse == null) {
				orderStatus = OrderStatus.FAILURE;
			}

			log.info("Buy order executed: {}", buyOrderResponse);
		} else if (Decision.SELL.getKey().equals(resultDto.decision())) {
			// 매도
			double currentPrice = marketPriceService.getCurrentPrice(MARKET);
			double btcBalance = getBtcBalance();
			OrderResponse sellOrderResponse = orderExecuteService.executeSellOrder(MARKET, btcBalance, currentPrice,
				resultDto.percentage());

			if (sellOrderResponse == null) {
				orderStatus = OrderStatus.FAILURE;
			}

			log.info("Sell order executed: {}", sellOrderResponse);
		} else {
			log.info("No action taken based on AI decision");
		}

		// 매매 정보 DB 등록
		List<AccountDto> newAccounts = accountInfoService.getAccounts();

		String krwBalance = newAccounts.stream()
			.filter(account -> KRW_CURRENCY.equals(account.currency()))
			.map(AccountDto::balance)
			.findFirst()
			.orElse("0");

		String btcBalance = newAccounts.stream()
			.filter(account -> BTC_CURRENCY.equals(account.currency()))
			.map(AccountDto::balance)
			.findFirst()
			.orElse("0");

		String btcAvgBuyPrice = newAccounts.stream()
			.filter(account -> BTC_CURRENCY.equals(account.currency()))
			.map(AccountDto::avgBuyPrice)
			.findFirst()
			.orElse("0");

		double btcKrwPrice = marketPriceService.getCurrentPrice(MARKET);

		TradeInfoDto tradeInfoDto = TradeInfoDto.of(
			resultDto.decision(),
			resultDto.percentage(),
			resultDto.reason(),
			krwBalance,
			btcBalance,
			btcAvgBuyPrice,
			btcKrwPrice,
			callReflectionContent,
			orderStatus
		);

		tradeDomainService.createTradeInfo(tradeInfoDto);
	}

	private double getAvailableBalance() {
		return accountInfoService.getAccounts().stream()
			.filter(account -> KRW_CURRENCY.equals(account.currency()))
			.findFirst()
			.map(account -> NumberUtils.parseBalance(account.balance()))
			.orElse(0.0);
	}

	private double getBtcBalance() {
		return accountInfoService.getAccounts().stream()
			.filter(account -> BTC_CURRENCY.equals(account.currency()))
			.findFirst()
			.map(account -> NumberUtils.parseBalance(account.balance()))
			.orElse(0.0);
	}

	private String resourceToString(Resource resource) {
		try {
			return IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_NOT_FOUND_SCHEMA);
		}
	}
}
