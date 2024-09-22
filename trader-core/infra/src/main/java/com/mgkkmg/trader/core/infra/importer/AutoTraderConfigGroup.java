package com.mgkkmg.trader.core.infra.importer;

import com.mgkkmg.trader.core.infra.config.JasyptConfig;
import com.mgkkmg.trader.core.infra.config.JpaConfig;
import com.mgkkmg.trader.core.infra.config.UpbitConfig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AutoTraderConfigGroup {

	JASYPT(JasyptConfig.class),
	JPA(JpaConfig.class),
	UPBIT(UpbitConfig.class)
	;

	private final Class<? extends AutoTraderConfig> configClass;
}
