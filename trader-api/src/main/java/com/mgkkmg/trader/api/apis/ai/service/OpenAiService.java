package com.mgkkmg.trader.api.apis.ai.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OpenAiService {

	private final OpenAiChatModel openAiChatModel;

	public OpenAiChatOptions getChatOptions() {

		// OpenAI Chat 옵션 설정
		return OpenAiChatOptions.builder()
			.withModel(ChatModel.GPT_4_O)
			.withTemperature(0.7)
			.build();
	}

	public OpenAiChatOptions getChatOptions(String schema) {

		// OpenAI Chat 옵션 설정
		return OpenAiChatOptions.builder()
			.withModel(ChatModel.GPT_4_O)
			.withTemperature(0.7)
			.withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, schema))
			.build();
	}

	public String callAi(String message, OpenAiChatOptions chatOptions) {
		var userMessage = new UserMessage(message);

		return call(userMessage, chatOptions);
	}

	public String callAi(String message, String filePath, OpenAiChatOptions chatOptions) {
		var imageResource = new PathResource(filePath);
		var userMessage = new UserMessage(message, new Media(MimeTypeUtils.IMAGE_PNG, imageResource));

		return call(userMessage, chatOptions);
	}

	private String call(UserMessage userMessage, OpenAiChatOptions chatOptions) {
		return openAiChatModel.call(new Prompt(userMessage, chatOptions)).getResult().getOutput().getContent();
	}
}
