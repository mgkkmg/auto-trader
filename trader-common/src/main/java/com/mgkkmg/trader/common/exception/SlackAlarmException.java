package com.mgkkmg.trader.common.exception;

import com.mgkkmg.trader.common.code.ErrorCode;

import lombok.Getter;

@Getter
public class SlackAlarmException extends BusinessException {

	private String value;

	public SlackAlarmException(String value) {
		this(value, ErrorCode.SLACK_ALARM_ERROR);
		this.value = value;
	}

	public SlackAlarmException(String value, ErrorCode errorCode) {
		super(value, errorCode);
		this.value = value;
	}
}
