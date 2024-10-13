package com.mgkkmg.trader.core.infra.alarm.slack;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RequestStorage {

	private ContentCachingRequestWrapper request;

	public void set(ContentCachingRequestWrapper request) {
		this.request = request;
	}

	public ContentCachingRequestWrapper get() {
		return request;
	}
}
