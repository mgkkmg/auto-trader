package com.mgkkmg.trader.api.config;

import org.springframework.context.annotation.Configuration;

import com.mgkkmg.trader.core.infra.AutoTraderConfigGroup;
import com.mgkkmg.trader.core.infra.EnableAutoTraderConfig;

@Configuration(proxyBeanMethods = false)
@EnableAutoTraderConfig({
	AutoTraderConfigGroup.JASYPT,
	AutoTraderConfigGroup.JPA
})
public class InfraConfig {
}
