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

		double initialKrwBalance = Double.parseDouble(trades.getFirst().krwBalance());
		double initialBtcBalance = Double.parseDouble(trades.getFirst().btcBalance());
		double initialBtcPrice = trades.getFirst().btcKrwPrice();

		// 총 투자금 (초기 자본 + 추가 입금)
		double totalInvested = initialKrwBalance + initialBtcBalance * initialBtcPrice;

		double previousKrwBalance = initialKrwBalance;
		double previousBtcBalance = initialBtcBalance;

		for (int i = 1; i < trades.size(); i++) {
			TradeInfoDto currentTrade = trades.get(i);

			// 예상 KRW 잔액 계산
			double expectedKrwBalance = previousKrwBalance;

			// 거래에 따른 KRW 및 BTC 잔액 변화 계산
			if (currentTrade.decision().equalsIgnoreCase("buy")) {
				double krwSpent = (double)currentTrade.percentage() / 100 * (previousKrwBalance + previousBtcBalance * currentTrade.btcKrwPrice());
				expectedKrwBalance -= krwSpent;
			} else if (currentTrade.decision().equalsIgnoreCase("sell")) {
				double btcToSell = (double)currentTrade.percentage() / 100 * previousBtcBalance;
				double krwGained = btcToSell * currentTrade.btcKrwPrice();
				expectedKrwBalance += krwGained;
			}

			// 실제 KRW 잔액과 예상 잔액의 차이 계산
			double krwDifference = Double.parseDouble(currentTrade.krwBalance()) - expectedKrwBalance;

			// 추가 입금이 있는지 확인
			if (krwDifference > 1e-6) { // 작은 오차 허용
				totalInvested += krwDifference;
			}

			// 상태 업데이트
			previousKrwBalance = Double.parseDouble(currentTrade.krwBalance());
			previousBtcBalance = Double.parseDouble(currentTrade.btcBalance());
		}

		// 최종 자산 계산
		TradeInfoDto lastTrade = trades.getLast();
		double finalKrwBalance = Double.parseDouble(lastTrade.krwBalance());
		double finalBtcBalance = Double.parseDouble(lastTrade.btcBalance());
		double finalBtcPrice = lastTrade.btcKrwPrice();
		double finalTotalAssets = finalKrwBalance + finalBtcBalance * finalBtcPrice;

		// 총 수익 및 수익률 계산
		double totalProfit = finalTotalAssets - totalInvested;

		log.info("총 투자금 (초기 자본 + 추가 입금): {} KRW", String.format("%.2f", totalInvested));
		log.info("최종 총 자산: {} KRW", String.format("%.2f", finalTotalAssets));
		log.info("총 수익: {} KRW", String.format("%.2f", totalProfit));

		return (totalProfit / totalInvested) * 100;
	}

	private static double calculateTotalBalance(TradeInfoDto trade) {
		double krwBalance = Double.parseDouble(trade.krwBalance());
		double btcBalance = Double.parseDouble(trade.btcBalance());
		double btcPrice = trade.btcKrwPrice();

		return krwBalance + btcBalance * btcPrice;
	}
}
