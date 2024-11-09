package com.mgkkmg.trader.common.response.coin;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccountResponse(
	String currency,
	String balance,
	String locked,
	@JsonProperty("avg_buy_price") String avgBuyPrice,
	@JsonProperty("avg_buy_price_modified") Boolean avgBuyPriceModified,
	@JsonProperty("unit_currency") String unitCurrency
) {
}
