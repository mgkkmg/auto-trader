package com.mgkkmg.trader.api.config;

import org.springframework.context.annotation.Configuration;

import com.mgkkmg.trader.core.infra.importer.AutoTraderConfigGroup;
import com.mgkkmg.trader.core.infra.importer.EnableAutoTraderConfig;

@Configuration(proxyBeanMethods = false)
@EnableAutoTraderConfig({
	AutoTraderConfigGroup.JASYPT,
	AutoTraderConfigGroup.JPA,
	AutoTraderConfigGroup.UPBIT
})
public class InfraConfig {
}
