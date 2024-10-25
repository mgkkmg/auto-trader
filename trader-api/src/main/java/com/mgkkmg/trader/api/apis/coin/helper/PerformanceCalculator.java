package com.mgkkmg.trader.api.apis.coin.helper;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.enums.Decision;
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

		// 계산을 위한 변수 초기화
		double initialTotalAsset = 0;
		double finalTotalAsset = 0;
		double cumulativeInvestment = 0;

		// 초기 자산 계산
		TradeInfoDto firstTrade = trades.getFirst();
		initialTotalAsset = Double.parseDouble(firstTrade.krwBalance()) + Double.parseDouble(firstTrade.btcBalance()) * firstTrade.btcKrwPrice();
		cumulativeInvestment += initialTotalAsset;

		// 이전 잔고 저장용 변수
		double prevKrwBalance = Double.parseDouble(firstTrade.krwBalance());
		double prevBtcBalance = Double.parseDouble(firstTrade.btcBalance());

		for (int i = 1; i < trades.size(); i++) {
			TradeInfoDto currentTrade = trades.get(i);
			String decision = currentTrade.decision();
			double percentage = currentTrade.percentage() / 100.0;

			// 입금 금액 추정
			double krwBalanceChange = Double.parseDouble(currentTrade.krwBalance()) - prevKrwBalance;
			double btcBalanceChange = Double.parseDouble(currentTrade.btcBalance()) - prevBtcBalance;

			if (Decision.BUY.getKey().equals(decision)) {
				// 매수 시 KRW 잔고가 감소해야 하지만 증가하면 입금으로 추정
				if (krwBalanceChange > 0) {
					cumulativeInvestment += krwBalanceChange;
				}
			} else if (Decision.SELL.getKey().equals(decision)) {
				// 매도 시 KRW 잔고 증가분에서 매도 금액을 제외한 나머지가 입금으로 추정
				double estimatedSellAmount = prevBtcBalance * percentage * currentTrade.btcKrwPrice();
				double deposit = krwBalanceChange - estimatedSellAmount;
				if (deposit > 0) {
					cumulativeInvestment += deposit;
				}
			}

			// 잔고 업데이트
			prevKrwBalance = Double.parseDouble(currentTrade.krwBalance());
			prevBtcBalance = Double.parseDouble(currentTrade.btcBalance());
		}

		// 최종 자산 계산
		TradeInfoDto lastTrade = trades.getLast();
		finalTotalAsset = Double.parseDouble(lastTrade.krwBalance()) + Double.parseDouble(lastTrade.btcBalance()) * lastTrade.btcKrwPrice();

		log.info("초기 총자산: {} KRW", String.format("%.2f", initialTotalAsset));
		log.info("최종 총자산: {} KRW", String.format("%.2f", finalTotalAsset));
		log.info("누적 투자금: {} KRW", String.format("%.2f", cumulativeInvestment));

		// 누적 수익률 계산
		return (finalTotalAsset - cumulativeInvestment) / cumulativeInvestment * 100;
	}

	private static double calculateTotalBalance(TradeInfoDto trade) {
		double krwBalance = Double.parseDouble(trade.krwBalance());
		double btcBalance = Double.parseDouble(trade.btcBalance());
		double btcPrice = trade.btcKrwPrice();

		return krwBalance + btcBalance * btcPrice;
	}
}
