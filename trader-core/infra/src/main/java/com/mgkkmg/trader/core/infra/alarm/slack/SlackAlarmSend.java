package com.mgkkmg.trader.core.infra.alarm.slack;

import static com.slack.api.webhook.WebhookPayloads.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.mgkkmg.trader.core.infra.alarm.slack.dto.SlackAlarmDto;
import com.slack.api.Slack;
import com.slack.api.model.Attachment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SlackAlarmSend {

	@Value("${slack.webhook.url}")
	private String url;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	private static final String ERROR_MESSAGE_TITLE = "🤯 *에러 발생*";
	private final Slack slack = Slack.getInstance();

	@Async("asyncExecutor")
	public void sendError(List<Attachment> attachments) {
		try {
			slack.send(url, payload(p -> p
				.text(ERROR_MESSAGE_TITLE)
				.channel("#")
				.attachments(attachments))
			);
		} catch (Exception e) {
			log.error("Slack Error Alarm Send Error: {}", e.getMessage());
		}
	}

	@Async("asyncExecutor")
	public void send(List<Attachment> attachments, SlackAlarmDto slackAlarmDto) {
		try {
			slack.send(url, payload(p -> p
				.text(slackAlarmDto.title())
				.channel(slackAlarmDto.channel())
				.username((!activeProfile.equals("prod") ? "[" + activeProfile.toUpperCase() + "] " : "") + slackAlarmDto.username())
				.attachments(attachments))
			);
		} catch (Exception e) {
			log.error("Slack Alarm Send Error: {}", e.getMessage());
		}
	}
}
