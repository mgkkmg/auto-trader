package com.mgkkmg.trader.common.exception;

import com.mgkkmg.trader.common.code.ErrorCode;

public class EmailDuplicateException extends DuplicateException {

	public EmailDuplicateException(final String email) {
		super(email, ErrorCode.EMAIL_DUPLICATE);
	}
}
