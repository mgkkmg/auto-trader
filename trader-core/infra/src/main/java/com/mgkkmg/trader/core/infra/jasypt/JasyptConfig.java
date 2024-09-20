package com.mgkkmg.trader.core.infra.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;

import com.mgkkmg.trader.core.infra.AutoTraderConfig;

public class JasyptConfig implements AutoTraderConfig {

	private static final String KEY = "default!23";

	@Bean
	public StringEncryptor jasyptStringEncryptor() {
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword(KEY);
		config.setAlgorithm("PBEWithMD5AndDES");
		config.setPoolSize(100);
		encryptor.setConfig(config);
		return encryptor;
	}
}
