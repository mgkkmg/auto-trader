package com.mgkkmg.trader.core.infra.alarm.slack.message.impl;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackMessageType;
import com.mgkkmg.trader.core.infra.alarm.slack.message.SlackMessage;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;

@Component
public class AlarmMessage implements SlackMessage {

	@Override
	public List<Attachment> createAttachments(List<LayoutBlock> data) {
		List<Attachment> attachments = new ArrayList<>();
		Attachment attachment = new Attachment();
		attachment.setColor("#e9a820");
		attachment.setBlocks(data);
		attachments.add(attachment);

		return attachments;
	}

	@Override
	public <T> List<LayoutBlock> createMessage(T param) {
		// Slack Message
		List<LayoutBlock> layoutBlockList = new ArrayList<>();
		layoutBlockList.add(section(section -> section.text(markdownText(param.toString()))));

		return layoutBlockList;
	}

	@Override
	public List<LayoutBlock> createErrorMessage(Exception e, ContentCachingRequestWrapper request) {
		return Collections.emptyList();
	}

	@Override
	public SlackMessageType getType() {
		return SlackMessageType.ALARM;
	}
}
