package com.mgkkmg.trader.core.infra.alarm.slack.message;

import java.util.List;

import org.springframework.web.util.ContentCachingRequestWrapper;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackMessageType;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;

public interface SlackMessage {

	List<Attachment> createAttachments(List<LayoutBlock> data);

	<T> List<LayoutBlock> createMessage(T param);

	List<LayoutBlock> createErrorMessage(Exception e, ContentCachingRequestWrapper request);

	SlackMessageType getType();
}
