package com.mgkkmg.trader.api.apis.coin.helper;

import org.ta4j.core.AnalysisCriterion;
import org.ta4j.core.BarSeries;
import org.ta4j.core.Strategy;
import org.ta4j.core.TradingRecord;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.MaximumDrawdownCriterion;
import org.ta4j.core.criteria.NumberOfWinningPositionsCriterion;
import org.ta4j.core.criteria.pnl.ReturnCriterion;

/**
 *  // 트레이딩 전략 정의
 * 	Strategy strategy = TradingStrategyBuilder.buildStrategy(dailySeries);
 *
 * 	// 백테스트 실행
 * 	Backtester.runBacktest(dailySeries, strategy);
 */
public class Backtester {

	/**
	 * 전략을 백테스트하고 결과를 출력합니다.
	 *
	 * @param series   BarSeries 객체
	 * @param strategy Strategy 객체
	 */
	public static void runBacktest(BarSeries series, Strategy strategy) {
		// 전략을 기반으로 거래 기록 생성
		BarSeriesManager seriesManager = new BarSeriesManager(series);
		TradingRecord tradingRecord = seriesManager.run(strategy);

		// 백테스트 결과 분석
		AnalysisCriterion totalReturn = new ReturnCriterion();
		AnalysisCriterion winRatio = new NumberOfWinningPositionsCriterion();
		// AnalysisCriterion numberOfTrades = new NumberOfTradesCriterion();
		// AnalysisCriterion profitFactor = new ProfitFactorCriterion();
		AnalysisCriterion maxDrawdown = new MaximumDrawdownCriterion();

		// 결과 출력
		System.out.println("=== 백테스트 결과 ===");
		System.out.println("총 수익률: " + totalReturn.calculate(series, tradingRecord) + " %");
		System.out.println("승률: " + winRatio.calculate(series, tradingRecord) + " %");
		// System.out.println("거래 횟수: " + numberOfTrades.calculate(series, tradingRecord));
		// System.out.println("수익비율 (Profit Factor): " + profitFactor.calculate(series, tradingRecord));
		System.out.println("최대 낙폭 (Max Drawdown): " + maxDrawdown.calculate(series, tradingRecord)+ " %");
	}
}
