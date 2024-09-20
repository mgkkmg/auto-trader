package com.mgkkmg.trader.common.httpinterface;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import com.mgkkmg.trader.common.client.UpbitApiClient;

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

	private RestClient createRestClient() {
		return RestClient.builder()
			.defaultStatusHandler(
				HttpStatusCode::is4xxClientError,
				(request, response) -> {
					log.error("Client Error Code={}", response.getStatusCode());
					log.error("Client Error Message={}", new String(response.getBody().readAllBytes()));
				})
			.defaultStatusHandler(
				HttpStatusCode::is5xxServerError,
				(request, response) -> {
					log.error("Server Error Code={}", response.getStatusCode());
					log.error("Server Error Message={}", new String(response.getBody().readAllBytes()));
				})
			.build();
	}
}
