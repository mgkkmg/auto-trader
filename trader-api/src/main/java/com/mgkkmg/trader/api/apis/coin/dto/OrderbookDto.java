package com.mgkkmg.trader.api.apis.coin.dto;

import java.util.List;

import com.mgkkmg.trader.common.response.coin.OrderbookResponse;

import lombok.Builder;

@Builder
public record OrderbookDto(
	String market,
	Long timestamp,
	Double totalAskSize,
	Double totalBidSize,
	List<OrderbookUnitDto> orderbookUnits,
	Double level
) {
	public record OrderbookUnitDto(
		Double askPrice,
		Double bidPrice,
		Double askSize,
		Double bidSize
	) {}

	public static OrderbookDto from(OrderbookResponse response) {
		List<OrderbookUnitDto> orderbookUnitDtos = response.orderbookUnits().stream()
			.map(unit -> new OrderbookUnitDto(
				unit.askPrice(),
				unit.bidPrice(),
				unit.askSize(),
				unit.bidSize()
			))
			.toList();

		return OrderbookDto.builder()
			.market(response.market())
			.timestamp(response.timestamp())
			.totalAskSize(response.totalAskSize())
			.totalBidSize(response.totalBidSize())
			.orderbookUnits(orderbookUnitDtos)
			.level(response.level())
			.build();
	}
}
