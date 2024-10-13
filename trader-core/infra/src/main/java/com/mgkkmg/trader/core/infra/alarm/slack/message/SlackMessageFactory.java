package com.mgkkmg.trader.core.infra.alarm.slack.message;

import java.util.List;

import org.springframework.stereotype.Component;

import com.mgkkmg.trader.common.exception.SlackAlarmException;
import com.mgkkmg.trader.core.infra.alarm.slack.SlackMessageType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SlackMessageFactory {

	private final List<SlackMessage> slackMessages;

	public SlackMessage findSlackMessage(SlackMessageType messageType) {
		return slackMessages.stream()
			.filter(type -> messageType.equals(type.getType()))
			.findFirst()
			.orElseThrow(() -> new SlackAlarmException("슬랙 메시지 타입이 존재하지 않습니다"));
	}
}
