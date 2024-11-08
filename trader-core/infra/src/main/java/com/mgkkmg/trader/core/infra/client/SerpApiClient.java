package com.mgkkmg.trader.core.infra.client;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.mgkkmg.trader.common.response.coin.NewsResultResponse;

public interface SerpApiClient {

	@GetExchange("https://serpapi.com/search.json")
	NewsResultResponse getNewsResult(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestParam(value = "params") MultiValueMap<String, Object> params
	);
}
