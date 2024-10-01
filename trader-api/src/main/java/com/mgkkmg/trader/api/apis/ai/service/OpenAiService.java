package com.mgkkmg.trader.api.apis.ai.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi.ChatCompletionRequest.ResponseFormat;
import org.springframework.ai.openai.api.OpenAiApi.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OpenAiService {

	private final OpenAiChatModel openAiChatModel;

	@Value("classpath:schemas/openai_coin_result_schema.json")
	private Resource coinSchemaResource;

	@Value("classpath:templates/prompt_coin.st")
	private Resource promptCoinResource;

	@Value("${chart.path}")
	private String chartPath;

	@Value("${chart.upbit.file-name}")
	private String fileName;

	public OpenAiChatOptions getCoinChatOptions() {
		final String schemaAsString;
		try {
			schemaAsString = IOUtils.toString(coinSchemaResource.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_NOT_FOUND_SCHEMA);
		}

		// OpenAI Chat 옵션 설정
		return OpenAiChatOptions.builder()
			.withModel(ChatModel.GPT_4_O_MINI)
			.withTemperature(0.7)
			.withResponseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, schemaAsString))
			.build();
	}

	public String callAi(String message, OpenAiChatOptions chatOptions) {
		final String promptAsString;
		try {
			promptAsString = IOUtils.toString(promptCoinResource.getInputStream(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_NOT_FOUND_SCHEMA);
		}

		var imageResource = new PathResource(chartPath + "/" + fileName);
		var userMessage = new UserMessage(promptAsString.replace("{message}", message),
			new Media(MimeTypeUtils.IMAGE_PNG, imageResource));

		return openAiChatModel.call(new Prompt(userMessage, chatOptions)).getResult().getOutput().getContent();

		// PromptTemplate promptTemplate = new PromptTemplate(promptCoinResource);
		// Prompt prompt = promptTemplate.create(Map.of("message", message), chatOptions);
		//
		// return openAiChatModel.call(prompt).getResult().getOutput().getContent();
	}
}
