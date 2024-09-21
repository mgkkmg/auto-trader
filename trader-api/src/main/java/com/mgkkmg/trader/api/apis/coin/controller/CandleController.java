package com.mgkkmg.trader.api.apis.coin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.usecase.CandleUseCase;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;
import com.mgkkmg.trader.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CandleController {

	private final CandleUseCase candleUseCase;

	@GetMapping("/api/v1/candles/days")
	public SuccessResponse<List<CandleMinuteResponse>> getCandlesMinutesInfo() {
		return SuccessResponse.of(candleUseCase.getCandlesMinutesInfo());
	}
}
