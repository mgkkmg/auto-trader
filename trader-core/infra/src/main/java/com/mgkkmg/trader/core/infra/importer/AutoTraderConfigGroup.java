package com.mgkkmg.trader.core.infra.importer;

import com.mgkkmg.trader.core.infra.jasypt.JasyptConfig;
import com.mgkkmg.trader.core.infra.Jpa.JpaConfig;

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
