package com.mgkkmg.trader.common.response.coin;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CandleDayResponse(
	String market,
	@JsonProperty("candle_date_time_utc") String candleDateTimeUtc,
	@JsonProperty("candle_date_time_kst") String candleDateTimeKst,
	@JsonProperty("opening_price") Double openingPrice,
	@JsonProperty("high_price") Double highPrice,
	@JsonProperty("low_price") Double lowPrice,
	@JsonProperty("trade_price") Double tradePrice,
	Long timestamp,
	@JsonProperty("candle_acc_trade_price") Double candleAccTradePrice,
	@JsonProperty("candle_acc_trade_volume") Double candleAccTradeVolume,
	@JsonProperty("prev_closing_price") Double prevClosingPrice,
	@JsonProperty("change_price") Double changePrice,
	@JsonProperty("converted_trade_price") Double convertedTradePrice
) {
}
