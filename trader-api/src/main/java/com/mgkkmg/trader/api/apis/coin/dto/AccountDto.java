package com.mgkkmg.trader.api.apis.coin.dto;

import com.mgkkmg.trader.common.response.coin.AccountResponse;

import lombok.Builder;

@Builder
public record AccountDto(
	String currency,
	String balance,
	String locked,
	String avgBuyPrice,
	Boolean avgBuyPriceModified,
	String unitCurrency
) {
	public static AccountDto from(AccountResponse response) {
		return AccountDto.builder()
			.currency(response.currency())
			.balance(response.balance())
			.locked(response.locked())
			.avgBuyPrice(response.avgBuyPrice())
			.avgBuyPriceModified(response.avgBuyPriceModified())
			.unitCurrency(response.unitCurrency())
			.build();
	}
}
