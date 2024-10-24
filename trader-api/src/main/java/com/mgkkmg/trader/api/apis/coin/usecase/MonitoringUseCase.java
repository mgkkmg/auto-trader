package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mgkkmg.trader.api.apis.coin.dto.MonitoringDto;
import com.mgkkmg.trader.api.apis.coin.helper.PerformanceCalculator;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;
import com.mgkkmg.trader.core.domain.domains.coin.service.TradeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@UseCase
public class MonitoringUseCase {

	private final TradeService tradeDomainService;

	public MonitoringDto getMonitoringInfo() {
		// 거래 정보 전체 조회
		List<TradeInfoDto> tradeHistory = tradeDomainService.getAllTradesExceptSkip();
		List<TradeInfoDto> tradeInfos = tradeDomainService.getTradeInfoFromLastDays(365);

		// 성과 계산
		double performance = PerformanceCalculator.getPerformance(tradeInfos);

		// 거래 결정 분포 계산
		Map<String, Long> decisionDistribution = tradeHistory.stream()
			.collect(Collectors.groupingBy(TradeInfoDto::decision, Collectors.counting()));

		return new MonitoringDto(performance, tradeHistory, decisionDistribution);
	}
}
