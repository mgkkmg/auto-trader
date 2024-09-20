package com.mgkkmg.trader.common.exception;

import com.mgkkmg.trader.common.response.code.ErrorCode;

public class JwtErrorException extends BusinessException {

    public JwtErrorException(final String message) {
        super(message, ErrorCode.CREATE_JWT_TOKEN_ERROR);
    }
}
