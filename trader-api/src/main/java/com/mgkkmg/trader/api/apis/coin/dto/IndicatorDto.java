package com.mgkkmg.trader.api.apis.coin.dto;

import lombok.Builder;

@Builder
public record IndicatorDto(
	String date,
	double open,
	double high,
	double low,
	double close,
	double volume,
	double value,
	double sma14,
	double rsi14,
	double macd,
	double signal,
	double ema21,
	double atr14,
	double obv,
	// double psar,
	// double bollingerUpper,
	// double bollingerLower,
	double stochasticD
) {
}
