package com.mgkkmg.trader.core.infra.common.importer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoTraderConfigImportSelector.class)
public @interface EnableAutoTraderConfig {

	AutoTraderConfigGroup[] value();
}
