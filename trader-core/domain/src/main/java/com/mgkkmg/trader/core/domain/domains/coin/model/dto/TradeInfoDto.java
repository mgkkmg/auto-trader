package com.mgkkmg.trader.core.domain.domains.coin.model.dto;

import com.mgkkmg.trader.core.domain.domains.coin.model.entity.TradeInfoEntity;
import com.mgkkmg.trader.core.domain.domains.coin.model.enums.OrderStatus;

public record TradeInfoDto(
	Long id,
	String decision,
	Integer percentage,
	String reason,
	String krwBalance,
	String btcBalance,
	String btcAvgBuyPrice,
	Double btcKrwPrice,
	String reflection,
	OrderStatus orderStatus
) {

	public static TradeInfoDto of(
		String decision,
		Integer percentage,
		String reason,
		String krwBalance,
		String btcBalance,
		String btcAvgBuyPrice,
		Double btcKrwPrice,
		String reflection,
		OrderStatus orderStatus
	) {
		return new TradeInfoDto(
			null,
			decision,
			percentage,
			reason,
			krwBalance,
			btcBalance,
			btcAvgBuyPrice,
			btcKrwPrice,
			reflection,
			orderStatus
		);
	}

	public static TradeInfoDto fromEntity(TradeInfoEntity entity) {
		return new TradeInfoDto(
			entity.getId(),
			entity.getDecision(),
			entity.getPercentage(),
			entity.getReason(),
			entity.getKrwBalance(),
			entity.getBtcBalance(),
			entity.getBtcAvgBuyPrice(),
			entity.getBtcKrwPrice(),
			entity.getReflection(),
			entity.getOrderStatus()
		);
	}
}