package com.mgkkmg.trader.api.config;

import org.springframework.context.annotation.Configuration;

import com.mgkkmg.trader.core.infra.common.importer.AutoTraderConfigGroup;
import com.mgkkmg.trader.core.infra.common.importer.EnableAutoTraderConfig;

@Configuration(proxyBeanMethods = false)
@EnableAutoTraderConfig({
	AutoTraderConfigGroup.JASYPT,
	AutoTraderConfigGroup.JPA
})
public class InfraConfig {
}
