package com.mgkkmg.trader.api.apis.coin.dto;

import com.mgkkmg.trader.common.response.coin.CandleMinuteResponse;

import lombok.Builder;

@Builder
public record CandleMinuteDto(
	String date,
	Double open,
	Double high,
	Double low,
	Double close,
	Double volume,
	Double value
) {
	public static CandleMinuteDto from(CandleMinuteResponse response) {
		return CandleMinuteDto.builder()
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
