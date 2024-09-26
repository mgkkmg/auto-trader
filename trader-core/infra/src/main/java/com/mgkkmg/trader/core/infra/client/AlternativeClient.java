package com.mgkkmg.trader.core.infra.client;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.mgkkmg.trader.common.response.coin.FearGreedIndexResponse;

/**
 * <a href="https://alternative.me/crypto/fear-and-greed-index/#api">Fear and Greed Index API</a>
 */
public interface AlternativeClient {

	@GetExchange("https://api.alternative.me/fng/")
	FearGreedIndexResponse getFearAndGreedIndex(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestParam(value="params") MultiValueMap<String, Object> params
	);
}
