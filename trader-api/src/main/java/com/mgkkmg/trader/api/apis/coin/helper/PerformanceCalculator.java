package com.mgkkmg.trader.api.apis.coin.helper;

import java.util.List;

import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;

public class PerformanceCalculator {

	private PerformanceCalculator() {
		throw new IllegalStateException("Utility class");
	}

	public static double getPerformance(List<TradeInfoDto> trades) {
		if (trades.isEmpty()) {
			return 0.0;
		}

		TradeInfoDto oldestTrade = trades.getLast();
		TradeInfoDto newestTrade = trades.getFirst();

		double initialBalance = calculateTotalBalance(oldestTrade);
		double finalBalance = calculateTotalBalance(newestTrade);

		return (finalBalance - initialBalance) / initialBalance * 100;
	}

	private static double calculateTotalBalance(TradeInfoDto trade) {
		double krwBalance = Double.parseDouble(trade.krwBalance());
		double btcBalance = Double.parseDouble(trade.btcBalance());
		double btcPrice = trade.btcKrwPrice();

		return krwBalance + btcBalance * btcPrice;
	}
}
