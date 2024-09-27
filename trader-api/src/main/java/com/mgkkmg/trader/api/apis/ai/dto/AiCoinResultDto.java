package com.mgkkmg.trader.api.apis.ai.dto;

public record AiCoinResultDto(
	String decision,
	Integer percentage,
	String reason
) {
}
