package com.mgkkmg.trader.api.apis.coin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DecisionType {

	BUY("buy"),
	SELL("sell"),
	HOLD("hold"),
	;

	private final String key;

	public static boolean isTypeByKey() {

		return true;
	}
}
