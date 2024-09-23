package com.mgkkmg.trader.api.apis.coin.helper;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
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

		List<IndicatorDto> dataList = new ArrayList<>();
		for (int i = 0; i < series.getBarCount(); i++) {
			Num close = series.getBar(i).getClosePrice();
			Num smaValue = sma.getValue(i);
			Num rsiValue = rsi.getValue(i);
			Num macdValue = macd.getValue(i);
			Num signalValue = macdSignal.getValue(i);

			// ZonedDateTime을 포맷팅된 문자열로 변환
			String formattedDate = series.getBar(i).getEndTime().format(OUTPUT_FORMATTER);

			IndicatorDto data = new IndicatorDto(
				formattedDate,
				close.doubleValue(),
				smaValue.isNaN() ? 0.0 : smaValue.doubleValue(),
				rsiValue.isNaN() ? 0.0 : rsiValue.doubleValue(),
				macdValue.isNaN() ? 0.0 : macdValue.doubleValue(),
				signalValue.isNaN() ? 0.0 : signalValue.doubleValue()
			);

			dataList.add(data);
		}

		return JsonUtils.toJson(dataList);
	}
}
