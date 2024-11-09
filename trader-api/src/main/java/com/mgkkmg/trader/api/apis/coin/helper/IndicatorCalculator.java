package com.mgkkmg.trader.api.apis.coin.helper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.ATRIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.ParabolicSarIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.StochasticOscillatorDIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.indicators.volume.OnBalanceVolumeIndicator;
import org.ta4j.core.num.Num;

import com.mgkkmg.trader.api.apis.coin.dto.IndicatorDto;
import com.mgkkmg.trader.common.util.JsonUtils;

public class IndicatorCalculator {

	private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	/**
	 * 단순 이동평균(SMA) 계산
	 *
	 * @param series TimeSeries 객체
	 * @param period 기간
	 * @return SMAIndicator 객체
	 */
	public static SMAIndicator calculateSMA(BarSeries series, int period) {
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		return new SMAIndicator(closePrice, period);
	}

	/**
	 * 상대강도지수(RSI) 계산
	 *
	 * @param series TimeSeries 객체
	 * @param period 기간
	 * @return RSIIndicator 객체
	 */
	public static RSIIndicator calculateRSI(BarSeries series, int period) {
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		return new RSIIndicator(closePrice, period);
	}

	/**
	 * MACD 계산
	 *
	 * @param series       TimeSeries 객체
	 * @param shortPeriod  단기 EMA 기간
	 * @param longPeriod   장기 EMA 기간
	 * @return MACDIndicator 객체
	 */
	public static MACDIndicator calculateMACD(BarSeries series, int shortPeriod, int longPeriod) {
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		return new MACDIndicator(closePrice, shortPeriod, longPeriod);
	}

	/**
	 * MACD 신호선 계산
	 *
	 * @param macd   MACDIndicator 객체
	 * @param period 신호선 EMA 기간
	 * @return EMAIndicator 객체 (신호선)
	 */
	public static EMAIndicator calculateMACDSignal(MACDIndicator macd, int period) {
		return new EMAIndicator(macd, period);
	}

	/**
	 * Exponential Moving Average (EMA) 계산
	 *
	 * @param series BarSeries 객체
	 * @param period 기간
	 * @return EMAIndicator 객체
	 */
	public static EMAIndicator calculateEMA(BarSeries series, int period) {
		ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
		return new EMAIndicator(closePrice, period);
	}

	/**
	 * Bollinger Bands Middle (SMA) 계산
	 *
	 * @param series BarSeries 객체
	 * @param period 기간
	 * @return BollingerBandsMiddleIndicator 객체
	 */
	public static BollingerBandsMiddleIndicator calculateBollingerMiddle(BarSeries series, int period) {
		SMAIndicator sma = calculateSMA(series, period);
		return new BollingerBandsMiddleIndicator(sma);
	}

	/**
	 * Bollinger Bands Upper 계산
	 *
	 * @param series     BarSeries 객체
	 * @param period     기간
	 * @param multiplier 배수 (Num 타입)
	 * @return BollingerBandsUpperIndicator 객체
	 */
	public static BollingerBandsUpperIndicator calculateBollingerUpper(BarSeries series, int period, Num multiplier) {
		BollingerBandsMiddleIndicator middle = calculateBollingerMiddle(series, period);
		StandardDeviationIndicator stdDev = new StandardDeviationIndicator(new ClosePriceIndicator(series), period);
		return new BollingerBandsUpperIndicator(middle, stdDev, multiplier);
	}

	/**
	 * Bollinger Bands Lower 계산
	 *
	 * @param series     BarSeries 객체
	 * @param period     기간
	 * @param multiplier 배수 (Num 타입)
	 * @return BollingerBandsLowerIndicator 객체
	 */
	public static BollingerBandsLowerIndicator calculateBollingerLower(BarSeries series, int period, Num multiplier) {
		BollingerBandsMiddleIndicator middle = calculateBollingerMiddle(series, period);
		StandardDeviationIndicator stdDev = new StandardDeviationIndicator(new ClosePriceIndicator(series), period);
		return new BollingerBandsLowerIndicator(middle, stdDev, multiplier);
	}

	/**
	 * ATR 계산 메소드
	 *
	 * @param series BarSeries 객체
	 * @param period 기간
	 * @return ATRIndicator 객체
	 */
	public static ATRIndicator calculateATR(BarSeries series, int period) {
		return new ATRIndicator(series, period);
	}

	/**
	 * Stochastic Oscillator K 계산 메소드
	 *
	 * @param series BarSeries 객체
	 * @param period 기간
	 * @return StochasticOscillatorKIndicator 객체
	 */
	public static StochasticOscillatorKIndicator calculateStochasticK(BarSeries series, int period) {
		return new StochasticOscillatorKIndicator(series, period);
	}

	/**
	 * Stochastic Oscillator D 계산 메소드
	 *
	 * @param series BarSeries 객체
	 * @param period 기간
	 * @return StochasticOscillatorDIndicator 객체
	 */
	public static StochasticOscillatorDIndicator calculateStochasticD(BarSeries series, int period) {
		StochasticOscillatorKIndicator stochasticK = calculateStochasticK(series, period);
		return new StochasticOscillatorDIndicator(stochasticK); // 일반적으로 3-period EMA 사용
	}

	/**
	 * On-Balance Volume (OBV) 계산 메소드
	 *
	 * @param series BarSeries 객체
	 * @return OnBalanceVolumeIndicator 객체
	 */
	public static OnBalanceVolumeIndicator calculateOBV(BarSeries series) {
		return new OnBalanceVolumeIndicator(series);
	}

	/**
	 * Parabolic SAR 계산 메소드
	 *
	 * @param series BarSeries 객체
	 * @return ParabolicSarIndicator 객체
	 */
	public static ParabolicSarIndicator calculateParabolicSAR(BarSeries series) {
		return new ParabolicSarIndicator(series);
	}

	/**
	 * 보조지표를 JSON 문자열로 반환하는 메소드 (컴팩트 JSON)
	 *
	 * @param series BarSeries 객체
	 * @return 컴팩트한 JSON 문자열
	 */
	public static String getIndicatorsAsJson(BarSeries series) {
		SMAIndicator sma = calculateSMA(series, 14);
		RSIIndicator rsi = calculateRSI(series, 14);
		MACDIndicator macd = calculateMACD(series, 12, 26);
		EMAIndicator macdSignal = calculateMACDSignal(macd, 9);
		EMAIndicator ema21 = calculateEMA(series, 21);
		ATRIndicator atr14 = calculateATR(series, 14);
		OnBalanceVolumeIndicator obv = calculateOBV(series);
		// ParabolicSarIndicator psar = calculateParabolicSAR(series);

		// Bollinger Bands 계산
		// Num bollingerMultiplier = series.numOf(2.0); // 일반적으로 2를 사용
		// BollingerBandsUpperIndicator bollingerUpper = calculateBollingerUpper(series, 20, bollingerMultiplier);
		// BollingerBandsLowerIndicator bollingerLower = calculateBollingerLower(series, 20, bollingerMultiplier);

		StochasticOscillatorDIndicator stochasticD = calculateStochasticD(series, 14);

		List<IndicatorDto> dataList = new ArrayList<>();

		for (int i = 0; i < series.getBarCount(); i++) {
			// ZonedDateTime을 포맷팅된 문자열로 변환
			String formattedDate = series.getBar(i).getEndTime().format(OUTPUT_FORMATTER);
			Num open = series.getBar(i).getOpenPrice();
			Num high = series.getBar(i).getHighPrice();
			Num low = series.getBar(i).getLowPrice();
			Num close = series.getBar(i).getClosePrice();
			Num volume = series.getBar(i).getVolume();
			Num value = series.getBar(i).getAmount();
			Num smaValue = sma.getValue(i);
			Num rsiValue = rsi.getValue(i);
			Num macdValue = macd.getValue(i);
			Num signalValue = macdSignal.getValue(i);
			Num emaValue = ema21.getValue(i);
			Num atrValue = atr14.getValue(i);
			Num obvValue = obv.getValue(i);
			// Num psarValue = psar.getValue(i);
			// Num bollingerUpperValue = bollingerUpper.getValue(i);
			// Num bollingerLowerValue = bollingerLower.getValue(i);
			Num stochasticDValue = stochasticD.getValue(i);

			IndicatorDto data = IndicatorDto.builder()
				.date(formattedDate)
				.open(open.doubleValue())
				.high(high.doubleValue())
				.low(low.doubleValue())
				.close(close.doubleValue())
				.volume(volume.doubleValue())
				.value(value.doubleValue())
				.sma14(smaValue.isNaN() ? 0.0 : smaValue.doubleValue())
				.rsi14(rsiValue.isNaN() ? 0.0 : rsiValue.doubleValue())
				.macd(macdValue.isNaN() ? 0.0 : macdValue.doubleValue())
				.signal(signalValue.isNaN() ? 0.0 : signalValue.doubleValue())
				.ema21(emaValue.isNaN() ? 0.0 : emaValue.doubleValue())
				.atr14(atrValue.isNaN() ? 0.0 : atrValue.doubleValue())
				.obv(obvValue.isNaN() ? 0.0 : obvValue.doubleValue())
				// .psar(psarValue.isNaN() ? 0.0 : psarValue.doubleValue())
				// .bollingerUpper(bollingerUpperValue.isNaN() ? 0.0 : bollingerUpperValue.doubleValue())
				// .bollingerLower(bollingerLowerValue.isNaN() ? 0.0 : bollingerLowerValue.doubleValue())
				.stochasticD(stochasticDValue.isNaN() ? 0.0 : stochasticDValue.doubleValue())
				.build();

			dataList.add(data);
		}

		return JsonUtils.toJson(dataList);
	}
}
