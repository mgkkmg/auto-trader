package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.service.CandleDayService;
import com.mgkkmg.trader.api.apis.coin.service.CandleMinuteService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class CandleUseCase {

	private final CandleMinuteService candleMinuteService;
	private final CandleDayService candleDayService;

	public List<CandleMinuteResponse> getCandlesMinutesInfo() {
		return candleMinuteService.getCandlesMinutes();
	}

	public List<CandleMinuteResponse> getCandlesDaysInfo() {
		return candleDayService.getCandlesDays();
	}
}
