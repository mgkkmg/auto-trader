package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.api.apis.coin.dto.OrderbookDto;
import com.mgkkmg.trader.common.response.coin.OrderbookResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderbookService {

	private final UpbitApiClient upbitApiClient;

	public List<OrderbookDto> getOrderbook(String market) {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("markets", market);

		List<OrderbookResponse> orderBookResponses = upbitApiClient.getOrderbook(MediaType.APPLICATION_JSON_VALUE, params);

		return orderBookResponses.stream()
			.map(OrderbookDto::from)
			.toList();
	}
}
