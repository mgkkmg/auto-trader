package com.mgkkmg.trader.api.apis.coin.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public record RssItemDto(
	String title,
	String pubDate
) {
	public static RssItemDto of(String title, Date pubDate) {
		Instant instant = pubDate.toInstant();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
			.withZone(ZoneId.of("GMT"));

		return new RssItemDto(title, formatter.format(instant));
	}
}
