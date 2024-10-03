package com.mgkkmg.trader.core.domain.domains.coin.model.dto;

import com.mgkkmg.trader.core.domain.domains.coin.model.entity.TradeInfoEntity;

public record TradeInfoDto(
	Long id,
	String decision,
	Integer percentage,
	String reason,
	String krwBalance,
	String btcBalance,
	String btcAvgBuyPrice,
	Double currentBtcPrice,
	String reflection
) {

	public static TradeInfoDto of(
		String decision,
		Integer percentage,
		String reason,
		String krwBalance,
		String btcBalance,
		String btcAvgBuyPrice,
		Double currentBtcPrice,
		String reflection
	) {
		return new TradeInfoDto(null, decision, percentage, reason, krwBalance, btcBalance, btcAvgBuyPrice, currentBtcPrice, reflection);
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
			entity.getReflection()
		);
	}
}
