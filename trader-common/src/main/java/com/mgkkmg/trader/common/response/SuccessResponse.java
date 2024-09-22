package com.mgkkmg.trader.common.response;

import org.springframework.http.HttpStatus;

import com.mgkkmg.trader.common.code.SuccessCode;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

    private static final int RESULT_STATUS = HttpStatus.OK.value();

    private int status;
    private String code;
    private String message;
    private T result;

    private SuccessResponse(final int status, final SuccessCode code) {
        this.status = status;
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    private SuccessResponse(final int status, final SuccessCode code, final T result) {
        this.status = status;
        this.message = code.getMessage();
        this.code = code.getCode();
        this.result = result;
    }

    public static SuccessResponse<Void> of() {
        return new SuccessResponse<>(RESULT_STATUS, SuccessCode.OK);
    }

    public static SuccessResponse<Void> of(int status, SuccessCode code) {
        return new SuccessResponse<>(status, code);
    }

    public static <T> SuccessResponse<T> of(T result) {
        return new SuccessResponse<>(RESULT_STATUS, SuccessCode.OK, result);
    }

    public static <T> SuccessResponse<T> of(int status, SuccessCode code, T result) {
        return new SuccessResponse<>(status, code, result);
    }
}
