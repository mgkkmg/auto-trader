package com.mgkkmg.trader.api.apis.coin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

public record IndicatorDto(
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	String date,
	double close,
	double sma14,
	double rsi14,
	double macd,
	double signal
) {
}
