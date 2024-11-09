package com.mgkkmg.trader.common.response.coin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public record FearGreedIndexResponse(
	String name,
	List<FearGreedData> data,
	Metadata metadata
) {
	public record FearGreedData(
		String value,
		@JsonProperty("value_classification") String valueClassification,
		String timestamp,
		@JsonInclude(JsonInclude.Include.NON_NULL)
		@JsonProperty("time_until_update") String timeUntilUpdate
	) {}

	public record Metadata(
		String error
	) {}
}
