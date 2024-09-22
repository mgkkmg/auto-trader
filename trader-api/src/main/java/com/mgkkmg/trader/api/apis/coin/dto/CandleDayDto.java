package com.mgkkmg.trader.api.apis.coin.dto;

import com.mgkkmg.trader.common.response.coin.CandleDayResponse;

import lombok.Builder;

@Builder
public record CandleDayDto(
	String date,
	Double open,
	Double high,
	Double low,
	Double close,
	Double volume,
	Double value
) {
	public static CandleDayDto from(CandleDayResponse response) {
		return CandleDayDto.builder()
			.date(response.candleDateTimeKst())
			.open(response.openingPrice())
			.high(response.highPrice())
			.low(response.lowPrice())
			.close(response.tradePrice())
			.volume(response.candleAccTradeVolume())
			.value(response.candleAccTradePrice())
			.build();
	}
}
