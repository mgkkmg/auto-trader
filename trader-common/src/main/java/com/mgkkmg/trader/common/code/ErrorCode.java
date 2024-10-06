package com.mgkkmg.trader.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode implements ResponseBaseCode {

	// Common
	INVALID_INPUT_VALUE("C-001", "Invalid Input Value"),
	METHOD_NOT_ALLOWED("C-002", "Method Not Allowed"),
	INTERNAL_SERVER_ERROR("C-004", "Server Error"),
	INVALID_TYPE_VALUE("C-005", "Invalid Type Value"),
	HANDLE_ACCESS_DENIED("C-006", "Access is Denied"),
	CREATE_JWT_TOKEN_ERROR("C-007", "Jwt Token error"),

	// Util
	JSON_PROCESS_ERROR("U-001", "Json processing exception"),
	JSON_NOT_FOUND_SCHEMA("U-002", "JSON Schema 파일을 찾을 수 없습니다."),
	CAPTURE_SCREENSHOT_ERROR("U-003", "Error capturing screenshot"),
	WEBDRIVER_SETUP_ERROR("U-004", "Invalid remote WebDriver URL"),

	// Business
	DUPLICATE("B-001", "Duplicate Value"),
	NOT_FOUND("B-002", "Entity Not Found"),

	// Member,
	EMAIL_DUPLICATE("M-001", "Duplicate Email Address"),
	NICKNAME_DUPLICATE("M-002", "Duplicate Nickname")
	;

	private final String code;
	private final String message;
}
