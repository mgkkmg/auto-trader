package com.mgkkmg.trader.api.apis.coin.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.common.response.coin.FearGreedIndexResponse;
import com.mgkkmg.trader.core.infra.client.AlternativeClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FearGreedIndexService {

	private final AlternativeClient alternativeClient;

	public FearGreedIndexResponse getFearAndGreedIndex() {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("limit", "14");

		return alternativeClient.getFearAndGreedIndex(MediaType.APPLICATION_JSON_VALUE, params);
	}
}
