package com.mgkkmg.trader.core.infra.alarm.slack;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.mgkkmg.trader.common.exception.SlackAlarmException;
import com.mgkkmg.trader.core.infra.alarm.slack.dto.SlackAlarmDto;
import com.mgkkmg.trader.core.infra.alarm.slack.message.SlackMessage;
import com.mgkkmg.trader.core.infra.alarm.slack.message.SlackMessageFactory;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SlackAlarmClient {

	private final RequestStorage requestStorage;

	private final SlackAlarmSend slackAlarmSend;

	private final SlackMessageFactory slackMessageFactory;

	public void sendErrorAlarm(Exception e) {
		SlackMessage message = slackMessageFactory.findSlackMessage(SlackMessageType.ERROR);
		List<LayoutBlock> layoutBlocks = message.createErrorMessage(e, requestStorage.get());
		List<Attachment> attachments = message.createAttachments(layoutBlocks);

		slackAlarmSend.sendError(attachments);
	}

	public void sendAlarmMessage(String paramStr, SlackAlarmDto slackAlarmDto, SlackMessageType slackMessageType) {
		createMessageAndSend(paramStr, slackAlarmDto, slackMessageType);
	}

	private <T> void createMessageAndSend(T param, SlackAlarmDto slackAlarmDto, SlackMessageType slackMessageType) {
		if (ObjectUtils.isEmpty(slackMessageType)) {
			throw new SlackAlarmException("Slack Message Type is required.");
		}

		SlackMessage message = slackMessageFactory.findSlackMessage(slackMessageType);
		List<LayoutBlock> layoutBlocks = message.createMessage(param);
		List<Attachment> attachments = message.createAttachments(layoutBlocks);

		slackAlarmSend.send(attachments, slackAlarmDto);
	}
}
