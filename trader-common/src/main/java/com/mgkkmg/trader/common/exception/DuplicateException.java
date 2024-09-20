package com.mgkkmg.trader.common.exception;

import com.mgkkmg.trader.common.response.code.ErrorCode;

import lombok.Getter;

@Getter
public class DuplicateException extends BusinessException {

	private String value;

	public DuplicateException(String value) {
		this(value, ErrorCode.DUPLICATE);
		this.value = value;
	}

	public DuplicateException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}
}
