package com.mgkkmg.trader.core.infra.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.mgkkmg.trader.core.infra.importer.AutoTraderConfig;

public class JasyptConfig implements AutoTraderConfig {

	@Value("${jasypt.encryptor.password}")
	private String key;

	@Bean
	public StringEncryptor jasyptStringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();

		config.setPassword(key); // 암/복호화 키
		config.setAlgorithm("PBEWithMD5AndDES"); // 암/복호화 알고리즘
		config.setKeyObtentionIterations("1000"); // 암호화 키를 얻기 위해 적요되는 해싱 반복 횟수
		config.setPoolSize("1"); // 암/복호화에 필요한 인스턴스 Pool Size, 0보다 크게 설정해야 함
		config.setProviderName("SunJCE"); // 프로바이더
		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator"); // salt 생성 클래스 지정
		config.setStringOutputType("base64"); // 인코딩
		encryptor.setConfig(config); // 설정 정보

		return encryptor;
	}
}
