package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.mgkkmg.trader.api.apis.coin.dto.CandleDayDto;
import com.mgkkmg.trader.api.apis.coin.dto.CandleMinuteDto;
import com.mgkkmg.trader.common.response.coin.CandleDayResponse;
import com.mgkkmg.trader.common.response.coin.CandleMinuteResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CandleService {

	private final UpbitApiClient upbitApiClient;

	public List<CandleMinuteDto> getCandlesMinutes() {
		final int intervalMinute = 60;
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("market", "KRW-BTC");
		params.add("count", 24);

		List<CandleMinuteResponse> candleMinuteResponses = upbitApiClient.getCandlesMinutes(MediaType.APPLICATION_JSON_VALUE, intervalMinute, params);

		return candleMinuteResponses.stream()
			.map(CandleMinuteDto::from)
			.toList();
	}

	public List<CandleDayDto> getCandlesDays() {
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		params.add("market", "KRW-BTC");
		params.add("count", 30);

		List<CandleDayResponse> candleDayResponses = upbitApiClient.getCandlesDays(MediaType.APPLICATION_JSON_VALUE, params);

		return candleDayResponses.stream()
			.map(CandleDayDto::from)
			.toList();
	}
}
