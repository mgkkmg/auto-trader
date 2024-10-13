package com.mgkkmg.trader.core.infra.alarm.slack.message.impl;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackMessageType;
import com.mgkkmg.trader.core.infra.alarm.slack.message.SlackMessage;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;

@Component
public class ErrorMessage implements SlackMessage {

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private static final String FILTER_STRING = "";

	@Override
	public List<Attachment> createAttachments(List<LayoutBlock> data) {
		List<Attachment> attachments = new ArrayList<>();
		Attachment attachment = new Attachment();
		attachment.setColor("#eb4034");
		attachment.setBlocks(data);
		attachments.add(attachment);

		return attachments;
	}

	@Override
	public <T> List<LayoutBlock> createMessage(T param) {
		return Collections.emptyList();
	}

	@Override
	public List<LayoutBlock> createErrorMessage(Exception e, ContentCachingRequestWrapper request) {
		StackTraceElement[] stacks = e.getStackTrace();

		List<LayoutBlock> layoutBlockList = new ArrayList<>();

		layoutBlockList.add(section(section -> section.text(markdownText("*Exception class:*\n" + e.getClass().getCanonicalName()))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Error Message:*\n" + e.getMessage()))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Request URI:*\n" + request.getRequestURL()))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Request Method:*\n" + request.getMethod()))));
		layoutBlockList.add(section(section -> section.text(markdownText("*요청 시간:*\n" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"))))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Request IP:*\n" + request.getRemoteAddr()))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Profile 정보:*\n" + activeProfile))));
		layoutBlockList.add(section(section -> section.text(markdownText("*Error Stack:*\n" + e))));
		layoutBlockList.add(divider());
		layoutBlockList.add(section(section -> section.text(markdownText(filterErrorStack(stacks)))));

		return layoutBlockList;
	}

	@Override
	public SlackMessageType getType() {
		return SlackMessageType.ERROR;
	}

	private String filterErrorStack(StackTraceElement[] stacks) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("```");
		for (StackTraceElement stack : stacks) {
			if (stack.toString().contains(FILTER_STRING)) {
				stringBuilder.append(stack).append("\n");
			}
		}
		stringBuilder.append("```");

		return stringBuilder.toString();
	}
}
