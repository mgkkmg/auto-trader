package com.mgkkmg.trader.core.infra;

import java.util.Arrays;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

class AutoTraderConfigImportSelector implements DeferredImportSelector {

	@Override
	public String[] selectImports(AnnotationMetadata metadata) {
		return Arrays.stream(getValues(metadata))
			.map(v -> v.getConfigClass().getName())
			.toArray(String[]::new);
	}

	private AutoTraderConfigGroup[] getValues(AnnotationMetadata metadata) {
		Map<String, Object> attributes = metadata.getAnnotationAttributes(EnableAutoTraderConfig.class.getName());
		return (AutoTraderConfigGroup[]) MapUtils.getObject(attributes, "value", new AutoTraderConfigGroup[]{});
	}
}
