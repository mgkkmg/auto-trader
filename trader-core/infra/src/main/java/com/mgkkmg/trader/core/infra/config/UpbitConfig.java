package com.mgkkmg.trader.core.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mgkkmg.trader.core.infra.importer.AutoTraderConfig;

import lombok.Getter;

@Getter
@Configuration
public class UpbitConfig implements AutoTraderConfig {

	@Value("${upbit.access-key}")
	private String accessKey;

	@Value("${upbit.secret-key}")
	private String secretKey;
}
