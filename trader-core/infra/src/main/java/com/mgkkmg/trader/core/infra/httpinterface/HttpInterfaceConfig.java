package com.mgkkmg.trader.core.infra.httpinterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;
import com.mgkkmg.trader.core.infra.client.AlternativeClient;
import com.mgkkmg.trader.core.infra.client.NewsApiClient;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class HttpInterfaceConfig {

	private final HttpInterfaceFactory httpInterfaceFactory;

	public HttpInterfaceConfig() {
		this.httpInterfaceFactory = new HttpInterfaceFactoryImpl();
	}

	@Bean
	public UpbitApiClient upbitApiClient() {
		return httpInterfaceFactory.create(UpbitApiClient.class, createRestClient());
	}

	@Bean
	public AlternativeClient alternativeClient() {
		return httpInterfaceFactory.create(AlternativeClient.class, createRestClient());
	}

	@Bean
	public NewsApiClient newsApiClient() {
		return httpInterfaceFactory.create(NewsApiClient.class, createRestClient());
	}

	private RestClient createRestClient() {
		return RestClient.builder()
			.defaultStatusHandler(
				HttpStatusCode::is4xxClientError,
				(request, response) -> {
					String errorMessage = new String(response.getBody().readAllBytes());
					log.error("Client Error Code={}", response.getStatusCode());
					log.error("Client Error Message={}", errorMessage);
					throw new BusinessException(errorMessage, ErrorCode.REST_API_ERROR);
				})
			.defaultStatusHandler(
				HttpStatusCode::is5xxServerError,
				(request, response) -> {
					String errorMessage = new String(response.getBody().readAllBytes());
					log.error("Server Error Code={}", response.getStatusCode());
					log.error("Server Error Message={}", errorMessage);
					throw new BusinessException(errorMessage, ErrorCode.REST_API_ERROR);
				})
			.build();
	}
}
