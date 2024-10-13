package com.mgkkmg.trader.api.common.handler;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mgkkmg.trader.common.annotation.SlackErrorAlarm;
import com.mgkkmg.trader.common.exception.BusinessException;
import com.mgkkmg.trader.common.exception.DuplicateException;
import com.mgkkmg.trader.common.exception.SlackAlarmException;
import com.mgkkmg.trader.common.response.ErrorResponse;
import com.mgkkmg.trader.common.code.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
	 *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
	 *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
		final MethodArgumentNotValidException e) {
		log.error("handle MethodArgumentNotValidException", e);

		final StringBuilder builder = getStringBindingResult(e.getBindingResult());
		final int status = HttpStatus.BAD_REQUEST.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INVALID_INPUT_VALUE, builder.toString()));
	}

	/**
	 * @ModelAttribut 으로 binding error 발생시 BindException 발생한다.
	 * ref https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args
	 */
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<ErrorResponse> handleBindException(final BindException e) {
		log.error("handle BindException", e);

		final StringBuilder builder = getStringBindingResult(e.getBindingResult());
		final int status = HttpStatus.BAD_REQUEST.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INVALID_INPUT_VALUE, builder.toString()));
	}

	/**
	 * enum type 일치하지 않아 binding 못할 경우 발생
	 * 주로 @RequestParam enum으로 binding 못했을 경우 발생
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		final MethodArgumentTypeMismatchException e) {
		log.error("handle MethodArgumentTypeMismatchException", e);

		Class<?> type = e.getRequiredType();
		assert type != null;

		final String fieldError;
		if (type.isEnum()) {
			fieldError = "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(
				type.getEnumConstants(), ", ");
		} else {
			fieldError = "The parameter " + e.getName() + " must have a value of type " + type.getSimpleName();
		}

		final int status = HttpStatus.BAD_REQUEST.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INVALID_TYPE_VALUE, fieldError));
	}

	/**
	 * 지원하지 않은 HTTP method 호출 할 경우 발생
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		final HttpRequestMethodNotSupportedException e) {
		log.error("handle HttpRequestMethodNotSupportedException", e);

		final int status = HttpStatus.METHOD_NOT_ALLOWED.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.METHOD_NOT_ALLOWED));
	}

	/**
	 * JSON 형식의 요청 데이터를 파싱하는 과정에서 발생하는 예외를 처리하는 메서드
	 *
	 * @see HttpMessageNotReadableException
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.warn("handle HttpMessageNotReadableException : {}", e.getMessage());

		final int status = HttpStatus.BAD_REQUEST.value();

		if (e.getCause() instanceof MismatchedInputException mismatchedInputException) {
			return ResponseEntity.status(HttpStatus.valueOf(status))
				.body(ErrorResponse.of(status, ErrorCode.INVALID_INPUT_VALUE,
					mismatchedInputException.getPath().getFirst().getFieldName() + " 필드의 값이 유효하지 않습니다."));
		}

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INVALID_TYPE_VALUE, e.getMessage()));
	}

	/**
	 * 잘못된 URL 호출 시
	 *
	 * @see NoHandlerFoundException
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNoHandlerFoundException(final NoHandlerFoundException e) {
		log.warn("handle NoHandlerFoundException", e);

		final int status = HttpStatus.NOT_FOUND.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.NOT_FOUND, e.getMessage()));
	}

	/**
	 * 존재하지 않는 URL 호출 시
	 *
	 * @see NoHandlerFoundException
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(final NoResourceFoundException e) {
		log.warn("handle NoResourceFoundException", e);

		final int status = HttpStatus.NOT_FOUND.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.NOT_FOUND, e.getMessage()));
	}

	/**
	 * API 호출 시 데이터를 반환할 수 없는 경우
	 *
	 * @return ResponseEntity<ErrorResponse>
	 */
	@ExceptionHandler(HttpMessageNotWritableException.class)
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotWritableException(
		final HttpMessageNotWritableException e) {
		log.warn("handle HttpMessageNotWritableException", e);

		final int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage()));
	}

	@SlackErrorAlarm
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
		log.error("Handle BusinessException", e);

		final int status = HttpStatus.BAD_GATEWAY.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, e.getErrorCode()));
	}

	@SlackErrorAlarm
	@ExceptionHandler(DuplicateException.class)
	protected ResponseEntity<ErrorResponse> handleDuplicationException(final DuplicateException e) {
		log.error("Handle DuplicationException", e);

		final int status = HttpStatus.CONFLICT.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, e.getErrorCode(), e.getValue()));
	}

	@SlackErrorAlarm
	@ExceptionHandler(SlackAlarmException.class)
	protected ResponseEntity<ErrorResponse> handleDuplicationException(final SlackAlarmException e) {
		log.error("Handle SlackAlarmException", e);

		final int status = HttpStatus.BAD_GATEWAY.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, e.getErrorCode(), e.getValue()));
	}

	@SlackErrorAlarm
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		log.error("Handle Exception", e);

		final int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

		return ResponseEntity.status(HttpStatus.valueOf(status))
			.body(ErrorResponse.of(status, ErrorCode.INTERNAL_SERVER_ERROR));
	}

	private static StringBuilder getStringBindingResult(BindingResult bindingResult) {
		StringBuilder builder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append("[");
			builder.append(fieldError.getField());
			builder.append("](은)는 ");
			builder.append(fieldError.getDefaultMessage());
			builder.append(". ");
		}

		builder.deleteCharAt(builder.length() - 1);
		return builder;
	}
}
