package com.mgkkmg.trader.api.common.error;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.mgkkmg.trader.api.support.CodeResponseFieldsSnippet;
import com.mgkkmg.trader.common.code.ErrorCode;
import com.mgkkmg.trader.common.code.ResponseBaseCode;
import com.mgkkmg.trader.common.code.SuccessCode;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class ErrorCodeDocumentationTest {

	@Autowired
	protected MockMvc mockMvc;

	@Test
	@DisplayName("ErrorCode 문서화")
	void errorCodeDocumentation() throws Exception {
		// given + when
		ResultActions result = mockMvc.perform(get("/errors")
			.accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(document("에러 코드",
			codeResponseFields("code-response", beneathPath("errorCodes"),
				attributes(key("title").value("에러 코드")),
				enumConvertFieldDescriptor(ErrorCode.values())
			)
		));
	}

	@Test
	@DisplayName("SuccessCode 문서화")
	void successCodeDocumentation() throws Exception {
		// given + when
		ResultActions result = mockMvc.perform(get("/successes")
			.accept(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());

		// docs
		result.andDo(document("성공 코드",
			codeResponseFields("code-response", beneathPath("successCodes"),
				attributes(key("title").value("성공 코드")),
				enumConvertFieldDescriptor(SuccessCode.values())
			)
		));
	}

	private FieldDescriptor[] enumConvertFieldDescriptor(ResponseBaseCode[] errorCodes) {
		return Arrays.stream(errorCodes)
			.map(enumType -> fieldWithPath(enumType.getCode()).description(enumType.getMessage()))
			.toArray(FieldDescriptor[]::new);
	}

	public static CodeResponseFieldsSnippet codeResponseFields(String type,
		PayloadSubsectionExtractor<?> subsectionExtractor,
		Map<String, Object> attributes, FieldDescriptor... descriptors) {
		return new CodeResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
			, true);
	}
}
