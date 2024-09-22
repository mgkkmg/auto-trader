package com.mgkkmg.trader.common.exception;

import com.mgkkmg.trader.common.code.ErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
