package com.mgkkmg.trader.api.support;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.common.response.code.ErrorCode;
import com.mgkkmg.trader.common.response.code.SuccessCode;

@RestController
public class CodeController {

	@GetMapping("/errors")
	public ResponseEntity<CodeView> getErrorCodes() {

		Map<String, String> errorCodes = Arrays.stream(ErrorCode.values())
			.collect(Collectors.toMap(ErrorCode::getCode, ErrorCode::getMessage));

		return new ResponseEntity<>(CodeView.builder().errorCodes(errorCodes).build(), HttpStatus.OK);
	}

	@GetMapping("/successes")
	public ResponseEntity<CodeView> getSuccessCodes() {

		Map<String, String> successCodes = Arrays.stream(SuccessCode.values())
			.collect(Collectors.toMap(SuccessCode::getCode, SuccessCode::getMessage));

		return new ResponseEntity<>(CodeView.builder().successCodes(successCodes).build(), HttpStatus.OK);
	}
}
