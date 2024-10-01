package com.mgkkmg.trader.api.apis.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AiCoinResultDto(
	@JsonProperty(required = true, value = "decision") String decision,
	@JsonProperty(required = true, value = "percentage") Integer percentage,
	@JsonProperty(required = true, value = "reason") String reason
) {
}
