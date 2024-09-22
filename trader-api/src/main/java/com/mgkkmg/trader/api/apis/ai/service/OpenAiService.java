package com.mgkkmg.trader.api.apis.ai.service;

import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OpenAiService {

	private final OpenAiChatModel chatModel;

	@Value("classpath:templates/prompt_coin.st")
	private Resource promptCoinResource;

	public String callAi(String message) {
		PromptTemplate promptTemplate = new PromptTemplate(promptCoinResource);
		Prompt prompt = promptTemplate.create(Map.of("message", message));

		return chatModel.call(prompt).getResult().getOutput().getContent();
	}
}
