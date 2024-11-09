package com.mgkkmg.trader.api.apis.coin.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.api.apis.coin.dto.RssItemDto;
import com.mgkkmg.trader.common.exception.BusinessException;
import com.mgkkmg.trader.common.response.coin.NewsResultResponse;
import com.mgkkmg.trader.common.response.coin.NewsResultsDto;
import com.mgkkmg.trader.core.infra.client.NewsApiClient;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NewsService {

	private final NewsApiClient newsApiClient;

	@Value("${serp-api.private-key}")
	private String apiKey;

	public List<NewsResultsDto> getSerpApiNewsHeadLines() throws BusinessException {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("engine", "google_news");
		params.add("q", "bitcoin OR btc");
		params.add("gl", "us");
		params.add("hl", "en");
		params.add("api_key", apiKey);

		NewsResultResponse newsResult = newsApiClient.getSerpApiNewsResult(MediaType.APPLICATION_JSON_VALUE, params);

		if ("Success".equals(newsResult.searchMetadata().status())) {
			return newsResult.newsResults().stream()
				.limit(5)
				.toList();
		}

		return Collections.emptyList();
	}

	public List<RssItemDto> getRssNewsHeadLines() {
		try {
			URL feedUrl = URI.create("https://news.google.com/rss/search?q=bitcoin+OR+btc+when:1d&hl=en&gl=US&ceid=US:en").toURL();

			// URL로부터 InputStream 가져오기
			HttpURLConnection httpURLConnection = (HttpURLConnection)feedUrl.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.connect();
			InputStream inputStream = httpURLConnection.getInputStream();

			SyndFeedInput input = new SyndFeedInput();
			SyndFeed feed = input.build(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream));

			return feed.getEntries().stream()
				.limit(5)
				.map(entry -> RssItemDto.of(entry.getTitle(), entry.getPublishedDate()))
				.toList();

		} catch (Exception e) {
			// throw new BusinessException(e.getMessage(), ErrorCode.PARSE_RSS_FAIL);
			return Collections.emptyList();
		}
	}
}
