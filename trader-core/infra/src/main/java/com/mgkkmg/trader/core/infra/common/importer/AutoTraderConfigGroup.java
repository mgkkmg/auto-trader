package com.mgkkmg.trader.core.infra.common.importer;

import com.mgkkmg.trader.core.infra.config.JasyptConfig;
import com.mgkkmg.trader.core.infra.config.JpaConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AutoTraderConfigGroup {

	JASYPT(JasyptConfig.class),
	JPA(JpaConfig.class),
	;

	private final Class<? extends AutoTraderConfig> configClass;
}
