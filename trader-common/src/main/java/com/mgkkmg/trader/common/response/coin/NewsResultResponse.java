package com.mgkkmg.trader.common.response.coin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewsResultResponse(
	@JsonProperty("news_results")
	List<NewsItemDto> newsResults
) {
}
