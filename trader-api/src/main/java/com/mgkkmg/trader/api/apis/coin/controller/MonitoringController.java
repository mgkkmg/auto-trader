package com.mgkkmg.trader.api.apis.coin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.dto.MonitoringDto;
import com.mgkkmg.trader.api.apis.coin.usecase.MonitoringUseCase;
import com.mgkkmg.trader.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/coin")
@RestController
public class MonitoringController {

	private final MonitoringUseCase monitoringUseCase;

	@GetMapping("/monitoring")
	public SuccessResponse<MonitoringDto> monitoring() {
		return SuccessResponse.of(monitoringUseCase.getMonitoringInfo());
	}
}
