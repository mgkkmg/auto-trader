package com.mgkkmg.trader.common.response;

import java.util.ArrayList;
import java.util.List;

import com.mgkkmg.trader.common.response.code.ErrorCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private int status;
    private String code;
    private String message;
    private List<String> values = new ArrayList<>();

    private ErrorResponse(final int status, final ErrorCode code) {
        this.status = status;
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    private ErrorResponse(final int status, final ErrorCode code, final List<String> values) {
        this.status = status;
        this.message = code.getMessage();
        this.code = code.getCode();
        this.values = values;
    }

    public static ErrorResponse of(int status, ErrorCode code) {
        return new ErrorResponse(status, code);
    }

    public static ErrorResponse of(int status, ErrorCode code, String value) {
        List<String> values = List.of(value.split(",\\s*"));
        return new ErrorResponse(status, code, values);
    }
}
