package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.common.client.UpbitApiClient;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CandlesMinuteService {

	private final UpbitApiClient upbitApiClient;

	public List<CandleMinuteResponse> getCandlesMinute() {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("market", "KRW-BTC");
		params.add("count", 200);

		return upbitApiClient.getCandlesMinute(MediaType.APPLICATION_JSON_VALUE, 5, params);
	}
}
