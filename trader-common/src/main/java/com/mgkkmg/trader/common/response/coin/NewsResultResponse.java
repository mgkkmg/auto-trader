package com.mgkkmg.trader.common.response.coin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewsResultResponse(
	@JsonProperty("search_metadata") SearchMetadataDto searchMetadata,
	@JsonProperty("news_results") List<NewsResultsDto> newsResults
) {
}
