package com.mgkkmg.globalutil.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Base64Utils {

	// 기본 Base64 인코더와 디코더
	private static final Base64.Encoder encoder = Base64.getEncoder();
	private static final Base64.Decoder decoder = Base64.getDecoder();

	/**
	 * 문자열을 Base64로 인코딩합니다.
	 *
	 * @param input 인코딩할 문자열
	 * @return Base64로 인코딩된 문자열
	 */
	public static String encode(String input) {
		return encoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Base64로 인코딩된 문자열을 디코딩합니다.
	 *
	 * @param base64Input 디코딩할 Base64 문자열
	 * @return 디코딩된 문자열
	 */
	public static String decode(String base64Input) {
		byte[] decodedBytes = decoder.decode(base64Input);
		return new String(decodedBytes, StandardCharsets.UTF_8);
	}

	/**
	 * 바이트 배열을 Base64로 인코딩합니다.
	 *
	 * @param input 인코딩할 바이트 배열
	 * @return Base64로 인코딩된 문자열
	 */
	public static String encode(byte[] input) {
		return encoder.encodeToString(input);
	}

	/**
	 * Base64로 인코딩된 문자열을 바이트 배열로 디코딩합니다.
	 *
	 * @param base64Input 디코딩할 Base64 문자열
	 * @return 디코딩된 바이트 배열
	 */
	public static byte[] decodeToBytes(String base64Input) {
		return decoder.decode(base64Input);
	}

	/**
	 * URL 및 파일 안전한 Base64 인코딩을 수행합니다.
	 *
	 * @param input 인코딩할 문자열
	 * @return URL 및 파일 안전한 Base64로 인코딩된 문자열
	 */
	public static String encodeUrlSafe(String input) {
		Base64.Encoder urlEncoder = Base64.getUrlEncoder();
		return urlEncoder.encodeToString(input.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * URL 및 파일 안전한 Base64로 인코딩된 문자열을 디코딩합니다.
	 *
	 * @param base64Input 디코딩할 Base64 문자열
	 * @return 디코딩된 문자열
	 */
	public static String decodeUrlSafe(String base64Input) {
		Base64.Decoder urlDecoder = Base64.getUrlDecoder();
		byte[] decodedBytes = urlDecoder.decode(base64Input);
		return new String(decodedBytes, StandardCharsets.UTF_8);
	}

	/**
	 * 파일을 Base64로 인코딩합니다.
	 *
	 * @param filePath 인코딩할 파일의 경로
	 * @return Base64로 인코딩된 문자열
	 * @throws IOException 파일 읽기 오류 시 발생
	 */
	public static String encodeFileToBase64(Path filePath) throws IOException {
		byte[] fileBytes = Files.readAllBytes(filePath);
		return encoder.encodeToString(fileBytes);
	}

	/**
	 * Base64로 인코딩된 문자열을 파일로 디코딩합니다.
	 *
	 * @param base64Data 디코딩할 Base64 문자열
	 * @param outputPath 디코딩된 파일을 저장할 경로
	 * @throws IOException 파일 쓰기 오류 시 발생
	 */
	public static void decodeBase64ToFile(String base64Data, Path outputPath) throws IOException {
		byte[] decodedBytes = decoder.decode(base64Data);
		Files.write(outputPath, decodedBytes);
	}
}
