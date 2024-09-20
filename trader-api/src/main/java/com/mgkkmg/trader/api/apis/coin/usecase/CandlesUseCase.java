package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.service.CandlesMinuteService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class CandlesUseCase {

	private final CandlesMinuteService candlesMinuteService;

	public List<CandleMinuteResponse> getCandlesMinuteInfo() {
		return candlesMinuteService.getCandlesMinute();
	}
}
