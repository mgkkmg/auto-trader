package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.common.response.CandleMinuteResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CandleDayService {

	private final UpbitApiClient upbitApiClient;

	public List<CandleMinuteResponse> getCandlesDays() {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("market", "KRW-BTC");
		params.add("count", 30);

		return upbitApiClient.getCandlesDays(MediaType.APPLICATION_JSON_VALUE, params);
	}
}
