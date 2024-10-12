package com.mgkkmg.trader.core.infra.config;

import static org.assertj.core.api.Assertions.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;

class JasyptConfigTest {

	private String ENCRYPT_KEY = System.getenv("JASYPT_ENCRYPTOR_PASSWORD");

	@Test
	void jasypt_암호화() {
		String plainText = "";

		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
		jasypt.setPassword(ENCRYPT_KEY);

		String encryptedText = jasypt.encrypt(plainText);
		String decryptedText = jasypt.decrypt(encryptedText);

		System.out.println(encryptedText);

		assertThat(plainText).isEqualTo(decryptedText);
	}

	@Test
	void jasypt_복호화() {
		String encryptedText = "";

		StandardPBEStringEncryptor jasypt = new StandardPBEStringEncryptor();
		jasypt.setPassword(ENCRYPT_KEY);

		String decryptedText = jasypt.decrypt(encryptedText);

		System.out.println(decryptedText);
	}
}