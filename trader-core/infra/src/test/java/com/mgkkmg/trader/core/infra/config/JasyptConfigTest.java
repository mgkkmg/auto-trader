package com.mgkkmg.trader.core.infra.config;

import static org.assertj.core.api.Assertions.*;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ContextConfiguration(classes = JasyptConfig.class)
@TestPropertySource(properties = "jasypt.encryptor.password=${ENCRYPT_KEY}")
class JasyptConfigTest {

	@Value("${jasypt.encryptor.password}")
	private String ENCRYPT_KEY;

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