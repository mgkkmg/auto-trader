package com.mgkkmg.trader.api.apis.coin.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.mgkkmg.trader.api.apis.coin.dto.Deposit;
import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfitAnalysis {

	private List<TradeInfoDto> records;
	private List<Deposit> deposits;
	private double initialAsset;
	private double finalAsset;
	private double totalDeposits;

	public ProfitAnalysis(List<TradeInfoDto> records) {
		this.records = records;
		this.deposits = new ArrayList<>();
		calculateInitialAndFinalAssets();
		detectDeposits();
		calculateTotalDeposits();
	}

	private void calculateInitialAndFinalAssets() {
		TradeInfoDto firstRecord = records.getFirst();
		TradeInfoDto lastRecord = records.getLast();

		initialAsset = calculateTotalAsset(firstRecord);
		finalAsset = calculateTotalAsset(lastRecord);
	}

	private double calculateTotalAsset(TradeInfoDto record) {
		return Double.parseDouble(record.krwBalance()) + (Double.parseDouble(record.btcBalance()) * record.btcKrwPrice());
	}

	private void detectDeposits() {
		double prevTotal = calculateTotalAsset(records.getFirst());

		for (int i = 1; i < records.size(); i++) {
			TradeInfoDto prevRecord = records.get(i - 1);
			TradeInfoDto currentRecord = records.get(i);

			double currentTotal = calculateTotalAsset(currentRecord);
			double prevTotalAtCurrentPrice = Double.parseDouble(prevRecord.krwBalance()) + (Double.parseDouble(prevRecord.btcBalance()) * currentRecord.btcKrwPrice());

			double priceChangeEffect = prevTotalAtCurrentPrice - prevTotal;
			double actualChange = currentTotal - prevTotal;

			// 입금 감지 (10,000원 이상의 차이가 있을 때)
			if (actualChange - priceChangeEffect > 10000) {
				deposits.add(new Deposit(currentRecord.createdAt(), actualChange - priceChangeEffect));
			}

			prevTotal = currentTotal;
		}
	}

	private void calculateTotalDeposits() {
		totalDeposits = deposits.stream()
			.mapToDouble(Deposit::amount)
			.sum();
	}

	private double getDepositsUntil(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

		return deposits.stream()
			.filter(d -> !LocalDateTime.parse(d.date(), formatter).isAfter(LocalDateTime.parse(date, formatter)))
			.mapToDouble(Deposit::amount)
			.sum();
	}

	public double analyzeProfit() {
		log.info("초기 자산: {} KRW", String.format("%.2f", initialAsset));
		log.info("총 입금액: {} KRW", String.format("%.2f", totalDeposits));
		log.info("최종 자산: {} KRW", String.format("%.2f", finalAsset));

		// 최종 수익률 계산
		return ((finalAsset - initialAsset - totalDeposits) / (initialAsset + totalDeposits)) * 100;

		// 일자별 수익률 출력
		// System.out.println("\n=== 일자별 수익률 ===");
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		// for (TradeInfoDto record : records) {
		// 	double depositsUntilNow = getDepositsUntil(record.createdAt());
		// 	double currentAsset = calculateTotalAsset(record);
		// 	double profitRate = ((currentAsset - initialAsset - depositsUntilNow) /
		// 		(initialAsset + depositsUntilNow)) * 100;
		//
		// 	System.out.printf("%s: %.2f%% (자산: %,.0f KRW)%n",
		// 		LocalDateTime.parse(record.createdAt(), formatter).toLocalDate(),
		// 		profitRate,
		// 		currentAsset);
		// }
	}
}
