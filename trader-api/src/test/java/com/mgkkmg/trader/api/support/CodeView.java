package com.mgkkmg.trader.api.support;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CodeView {

	private Map<String, String> errorCodes;
	private Map<String, String> successCodes;
}
