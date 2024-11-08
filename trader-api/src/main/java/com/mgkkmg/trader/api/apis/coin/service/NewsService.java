package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.common.response.coin.NewsItemDto;
import com.mgkkmg.trader.common.response.coin.NewsResultResponse;
import com.mgkkmg.trader.core.infra.client.SerpApiClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NewsService {

	private final SerpApiClient serpApiClient;

	@Value("${serp-api.private-key}")
	private String apiKey;

	public List<NewsItemDto> getNewsHeadLines() {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("engine", "google_news");
		params.add("q", "btc");
		params.add("gl", "us");
		params.add("hl", "en");
		params.add("api_key", apiKey);

		NewsResultResponse newsResult = serpApiClient.getNewsResult(MediaType.APPLICATION_JSON_VALUE, params);

		return newsResult.newsResults().stream()
			.limit(5)
			.toList();
	}
}
