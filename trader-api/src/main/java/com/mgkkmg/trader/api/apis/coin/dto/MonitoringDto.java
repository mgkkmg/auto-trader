package com.mgkkmg.trader.api.apis.coin.dto;

import java.util.List;
import java.util.Map;

import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;

public record MonitoringDto(
	double performance,
	List<TradeInfoDto> tradeHistory,
	Map<String, Long> decisionDistribution
) {
}
