package com.mgkkmg.trader.api.apis.coin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.usecase.CandlesUseCase;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;
import com.mgkkmg.trader.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CandlesController {

	private final CandlesUseCase candlesUseCase;

	@GetMapping("/api/v1/candles/days")
	public SuccessResponse<List<CandleMinuteResponse>> getCandlesMinuteInfo() {
		return SuccessResponse.of(candlesUseCase.getCandlesMinuteInfo());
	}
}
