package com.mgkkmg.trader.api.apis.coin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Decision {

	BUY("buy"),
	SELL("sell"),
	HOLD("hold"),
	;

	private final String key;
}
