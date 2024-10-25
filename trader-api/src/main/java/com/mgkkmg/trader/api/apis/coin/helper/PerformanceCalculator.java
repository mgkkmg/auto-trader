package com.mgkkmg.trader.api.apis.coin.helper;

import java.util.List;

import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public static double getPerformance2(List<TradeInfoDto> trades) {
		if (trades.isEmpty()) {
			return 0.0;
		}

		double initialKRW = Double.parseDouble(trades.getFirst().krwBalance());
		double initialBTCValue = Double.parseDouble(trades.getFirst().btcBalance()) * trades.getFirst().btcKrwPrice();
		double initialTotal = initialKRW + initialBTCValue;

		double totalDeposits = 0;
		double totalWithdrawals = 0;

		for (int i = 1; i < trades.size(); i++) {
			TradeInfoDto prev = trades.get(i - 1);
			TradeInfoDto curr = trades.get(i);

			double btcChange = Double.parseDouble(curr.btcBalance()) - Double.parseDouble(prev.btcBalance());
			double btcKrwPrice = curr.btcKrwPrice();

			double expectedKRWBalance = Double.parseDouble(prev.krwBalance()) - btcChange * btcKrwPrice;
			double actualKRWBalance = Double.parseDouble(curr.krwBalance());
			double krwDifference = actualKRWBalance - expectedKRWBalance;

			if (Math.abs(krwDifference) > 1) { // 입출금으로 간주되는 임계값
				if (krwDifference > 0) {
					totalDeposits += krwDifference;
				} else {
					totalWithdrawals += -krwDifference;
				}
			}
		}

		TradeInfoDto lastTransaction = trades.getLast();
		double finalKRW = Double.parseDouble(lastTransaction.krwBalance());
		double finalBTCValue = Double.parseDouble(lastTransaction.btcBalance()) * lastTransaction.btcKrwPrice();
		double finalTotal = finalKRW + finalBTCValue;

		double netInvestment = initialTotal + totalDeposits - totalWithdrawals;
		double profit = finalTotal - netInvestment;

		return (profit / netInvestment) * 100;
	}

	private static double calculateTotalBalance(TradeInfoDto trade) {
		double krwBalance = Double.parseDouble(trade.krwBalance());
		double btcBalance = Double.parseDouble(trade.btcBalance());
		double btcPrice = trade.btcKrwPrice();

		return krwBalance + btcBalance * btcPrice;
	}
}
