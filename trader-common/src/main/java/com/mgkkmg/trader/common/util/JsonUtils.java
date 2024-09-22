package com.mgkkmg.trader.common.util;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.exception.BusinessException;

public class JsonUtils {

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String toJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_PROCESS_ERROR);
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, clazz);
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_PROCESS_ERROR);
		}
	}

	public static <T> List<T> fromJsonList(String json, Class<T> clazz) {
		try {
			return objectMapper.readValue(json, new TypeReference<List<T>>(){});
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_PROCESS_ERROR);
		}
	}

	public static <T> T fromJson(String json, TypeReference<T> typeReference) {
		try {
			return objectMapper.readValue(json, typeReference);
		} catch (JsonProcessingException e) {
			throw new BusinessException(e.getMessage(), ErrorCode.JSON_PROCESS_ERROR);
		}
	}
}
