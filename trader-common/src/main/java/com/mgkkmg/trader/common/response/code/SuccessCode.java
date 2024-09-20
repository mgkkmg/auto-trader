package com.mgkkmg.trader.common.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements ResponseBaseCode {

	OK("S-001", "완료"),
	CREATED("S-002", "등록 완료"),
	;

	private final String code;
	private final String message;
}
