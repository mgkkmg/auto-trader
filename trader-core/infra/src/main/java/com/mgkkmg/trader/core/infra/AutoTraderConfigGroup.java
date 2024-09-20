package com.mgkkmg.trader.core.infra;

import com.mgkkmg.trader.core.infra.jasypt.JasyptConfig;
import com.mgkkmg.trader.core.infra.jpa.JpaConfig;

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
