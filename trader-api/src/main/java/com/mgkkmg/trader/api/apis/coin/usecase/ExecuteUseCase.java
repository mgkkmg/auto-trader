package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import org.springframework.ai.openai.OpenAiChatOptions;
import org.ta4j.core.BarSeries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mgkkmg.globalutil.util.NumberUtils;
import com.mgkkmg.trader.api.apis.ai.dto.AiCoinResultDto;
import com.mgkkmg.trader.api.apis.ai.service.OpenAiService;
import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.api.apis.coin.enums.Decision;
import com.mgkkmg.trader.api.apis.coin.helper.IndicatorCalculator;
import com.mgkkmg.trader.api.apis.coin.service.AccountInfoService;
import com.mgkkmg.trader.api.apis.coin.service.CandleService;
import com.mgkkmg.trader.api.apis.coin.service.FearGreedIndexService;
import com.mgkkmg.trader.api.apis.coin.service.MarketPriceService;
import com.mgkkmg.trader.api.apis.coin.service.OrderService;
import com.mgkkmg.trader.api.apis.coin.service.OrderbookService;
import com.mgkkmg.trader.api.apis.coin.service.TaService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.response.coin.OrderResponse;
import com.mgkkmg.trader.common.util.JsonUtils;

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
	private final OpenAiService openAiService;
	private final OrderService orderService;
	private final MarketPriceService marketPriceService;

	private static final String MARKET = "KRW-BTC";
	private static final String KRW_CURRENCY = "KRW";
	private static final String BTC_CURRENCY = "BTC";

	public void execute() {
		// 현재 투자 상태 조회
		List<AccountDto> accounts = accountInfoService.getAccounts().stream()
			.filter(account -> KRW_CURRENCY.equals(account.currency()) || BTC_CURRENCY.equals(account.currency()))
			.toList();
		String balance = JsonUtils.toJson(accounts);

		// 오더북(호가 데이터) 조회
		String orderbook = JsonUtils.toJson(orderbookService.getOrderbook(MARKET));

		// 30일 일봉 데이터 조회
		String dailyCandle = JsonUtils.toJson(candleService.getCandlesDays(MARKET));

		// 24시간 시간봉 데이터 조회
		String hourlyCandle = JsonUtils.toJson(candleService.getCandlesMinutes(MARKET));

		// 보조 지표 넣기
		BarSeries dailySeries = taService.createBarSeries(JsonUtils.fromJson(dailyCandle, new TypeReference<>() {}), true);
		BarSeries hourlySeries = taService.createBarSeries(JsonUtils.fromJson(hourlyCandle, new TypeReference<>() {}), false);

		String dailyCandleWithIndicator = IndicatorCalculator.getIndicatorsAsJson(dailySeries);
		String hourlyCandleWithIndicator = IndicatorCalculator.getIndicatorsAsJson(hourlySeries);

		// 공포 탐욕 지수
		String fearGreedIndex = JsonUtils.toJson(fearGreedIndexService.getFearAndGreedIndex());

		// 최신 뉴스

		// AI 호출 및 결과 받기
		String message = "Current investment status: " + balance + "\n"
			+ "Orderbook: " + orderbook + "\n"
			+ "Daily OHLCV with indicators (30 days): " + dailyCandleWithIndicator + "\n"
			+ "Hourly OHLCV with indicators (24 hours): " + hourlyCandleWithIndicator + "\n"
			+ "Fear and Greed Index: " + fearGreedIndex;

		System.out.println("message :" + message);

		OpenAiChatOptions coinChatOptions = openAiService.getCoinChatOptions();
		String outputContent = openAiService.callAi(message, coinChatOptions);
		AiCoinResultDto resultDto = JsonUtils.fromJson(outputContent, AiCoinResultDto.class);

		log.info("decision: {}", resultDto.decision());
		log.info("percentage: {}", resultDto.percentage());
		log.info("reason: {}", resultDto.reason());

		// 결과에 따른 주문
		if (Decision.BUY.getKey().equals(resultDto.decision())) {
			// 매수
			double currentPrice = marketPriceService.getCurrentPrice(MARKET);
			double availableBalance = getAvailableBalance();
			OrderResponse buyOrderResponse = orderService.executeBuyOrder(MARKET, availableBalance, currentPrice, resultDto.percentage());
			log.info("Buy order executed: {}", buyOrderResponse);
		} else if (Decision.SELL.getKey().equals(resultDto.decision())) {
			// 매도
			double btcBalance = getBtcBalance();
			OrderResponse sellOrderResponse = orderService.executeSellOrder(MARKET, btcBalance, resultDto.percentage());
			log.info("Sell order executed: {}", sellOrderResponse);
		} else {
			log.info("No action taken based on AI decision");
		}
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
}
