package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import org.ta4j.core.BarSeries;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mgkkmg.trader.api.apis.ai.dto.AiCoinResultDto;
import com.mgkkmg.trader.api.apis.ai.service.OpenAiService;
import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.api.apis.coin.helper.IndicatorCalculator;
import com.mgkkmg.trader.api.apis.coin.service.AccountInfoService;
import com.mgkkmg.trader.api.apis.coin.service.CandleService;
import com.mgkkmg.trader.api.apis.coin.service.OrderbookService;
import com.mgkkmg.trader.api.apis.coin.service.TaService;
import com.mgkkmg.trader.common.annotation.UseCase;
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
	private final OpenAiService openAiService;
	private final TaService taService;

	public void execute() {
		// 현재 투자 상태 조회
		List<AccountDto> accounts = accountInfoService.getAccounts().stream()
			.filter(account -> "BTC".equals(account.currency()) || "KRW".equals(account.currency()))
			.toList();
		String balance = JsonUtils.toJson(accounts);

		// 오더북(호가 데이터) 조회
		String orderbook = JsonUtils.toJson(orderbookService.getOrderbook());

		// 30일 일봉 데이터 조회
		String dailyCandle = JsonUtils.toJson(candleService.getCandlesDays());

		// 24시간 시간봉 데이터 조회
		String hourlyCandle = JsonUtils.toJson(candleService.getCandlesMinutes());

		// 보조 지표 넣기
		// 1. 데이터 시리즈 로드
		BarSeries dailySeries = taService.createBarSeries(JsonUtils.fromJson(dailyCandle, new TypeReference<>() {}), true);
		BarSeries hourlySeries = taService.createBarSeries(JsonUtils.fromJson(hourlyCandle, new TypeReference<>() {}), false);

		// 시리즈 정보 출력
		log.info("Daily Series Bars: {}", dailySeries.getBarCount());
		log.info("Hourly Series Bars: {}", hourlySeries.getBarCount());

		System.out.println("getIndicatorsAsJson: " + IndicatorCalculator.getIndicatorsAsJson(dailySeries));

		// AI 호출 및 결과 받기
		// String message = "Current investment status: " + balance + "\n"
		// 	+ "Orderbook: " + orderbook + "\n"
		// 	+ "Daily OHLCV (30 days): " + dailyCandle + "\n"
		// 	+ "Hourly OHLCV (24 hours): " + hourlyCandle;
		// String outputContent = openAiService.callAi(message);
		// AiCoinResultDto resultDto = JsonUtils.fromJson(outputContent, AiCoinResultDto.class);
		//
		// // 결과에 따른 주문
		// if ("buy".equals(resultDto.decision())) {
		// 	// 매수
		// } else if ("sell".equals(resultDto.decision())) {
		// 	// 매도
		// }
		//
		// log.info("decision: {}", resultDto.decision());
		// log.info("reason: {}", resultDto.reason());
	}
}
