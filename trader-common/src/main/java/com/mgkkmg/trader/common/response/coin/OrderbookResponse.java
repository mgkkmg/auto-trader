package com.mgkkmg.trader.common.response.coin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrderbookResponse(
	String market,
	Long timestamp,
	@JsonProperty("total_ask_size")
	Double totalAskSize,
	@JsonProperty("total_bid_size")
	Double totalBidSize,
	@JsonProperty("orderbook_units")
	List<OrderbookUnit> orderbookUnits,
	Double level
) {
	public record OrderbookUnit(
		@JsonProperty("ask_price")
		Double askPrice,
		@JsonProperty("bid_price")
		Double bidPrice,
		@JsonProperty("ask_size")
		Double askSize,
		@JsonProperty("bid_size")
		Double bidSize
	) {}
}
