package com.mgkkmg.trader.core.infra.alarm.slack.dto;

public record SlackAlarmDto(
	String title,
	String channel,
	String username
) {
	public static SlackAlarmDto of(String title, String channel, String username) {
		return new SlackAlarmDto(title, channel, username);
	}
}
