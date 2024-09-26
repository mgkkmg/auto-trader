package com.mgkkmg.trader.api.apis.coin.helper;

import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseStrategy;
import org.ta4j.core.Rule;
import org.ta4j.core.Strategy;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import org.ta4j.core.rules.OverIndicatorRule;
import org.ta4j.core.rules.UnderIndicatorRule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TradingStrategyBuilder {

	/**
	 * EMA(21), MACD(12,26,9), RSI(14)를 기반으로 한 트레이딩 전략을 정의합니다.
	 *
	 * @param series BarSeries 객체
	 * @return Strategy 객체
	 */
	public static Strategy buildStrategy(BarSeries series) {
		// 지표 계산
		RSIIndicator rsi14 = IndicatorCalculator.calculateRSI(series, 14);
		MACDIndicator macd = IndicatorCalculator.calculateMACD(series, 12, 26);
		EMAIndicator macdSignal = IndicatorCalculator.calculateMACDSignal(macd, 9);
		EMAIndicator ema21 = IndicatorCalculator.calculateEMA(series, 21);

		// 전략 규칙 정의

		// 매수 규칙:
		// 1. 현재 가격이 EMA(21) 위에 있어야 함
		// 2. RSI가 30 이하로 떨어져 과매도 상태일 때
		// 3. MACD가 신호선을 상향 돌파할 때
		Rule buyingRule = new OverIndicatorRule(new ClosePriceIndicator(series), ema21)
			.and(new UnderIndicatorRule(rsi14, 60))
			.and(new CrossedUpIndicatorRule(macd, macdSignal));

		// 매도 규칙:
		// 1. 현재 가격이 EMA(21) 아래에 있어야 함
		// 2. RSI가 70 이상으로 올라가 과매수 상태일 때
		// 3. MACD가 신호선을 하향 돌파할 때
		Rule sellingRule = new UnderIndicatorRule(new ClosePriceIndicator(series), ema21)
			.and(new OverIndicatorRule(rsi14, 70))
			.and(new CrossedDownIndicatorRule(macd, macdSignal));

		// 전략 생성
		return new BaseStrategy("EMA_MACD_RSI_Strategy", buyingRule, sellingRule);
	}
}
