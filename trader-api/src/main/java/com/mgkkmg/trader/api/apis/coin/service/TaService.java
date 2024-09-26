package com.mgkkmg.trader.api.apis.coin.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.num.DecimalNum;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TaService {
	private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");
	private static final DateTimeFormatter KST_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	public BarSeries createBarSeries(List<JsonNode> candles, boolean isDaily) {
		BarSeries series = new BaseBarSeries();

		// Iterate in reverse order to have chronological order
		for (int i = candles.size() - 1; i >= 0; i--) {
			JsonNode candle = candles.get(i);

			// Parse timestamp
			String timestamp = candle.get("date").asText();
			LocalDateTime localDateTime = LocalDateTime.parse(timestamp, KST_FORMATTER);
			ZonedDateTime endTime = localDateTime.atZone(KST_ZONE);

			// Open, High, Low, Close, Volume
			double open = candle.get("open").asDouble();
			double high = candle.get("high").asDouble();
			double low = candle.get("low").asDouble();
			double close = candle.get("close").asDouble();
			double volume = candle.get("volume").asDouble();
			double value = candle.get("value").asDouble();

			// Define the bar's time period
			Duration duration = isDaily ? Duration.ofDays(1) : Duration.ofHours(1);

			Bar bar = BaseBar.builder()
				.timePeriod(duration)
				.endTime(endTime)
				.openPrice(DecimalNum.valueOf(open))
				.highPrice(DecimalNum.valueOf(high))
				.lowPrice(DecimalNum.valueOf(low))
				.closePrice(DecimalNum.valueOf(close))
				.volume(DecimalNum.valueOf(volume))
				.amount(DecimalNum.valueOf(value))
				.build();

			series.addBar(bar);
		}

		return series;
	}
}
