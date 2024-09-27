package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mgkkmg.trader.common.response.coin.TickerResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MarketPriceService {

	private final UpbitApiClient upbitApiClient;

	public double getCurrentPrice(String market) {
		List<TickerResponse> tickerResponses = upbitApiClient.getTicker(MediaType.APPLICATION_JSON_VALUE, market);
		return tickerResponses.stream()
			.filter(ticker -> market.equals(ticker.market()))
			.findFirst()
			.map(TickerResponse::tradePrice)
			.orElse(0.0);
	}
}
