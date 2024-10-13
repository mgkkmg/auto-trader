package com.mgkkmg.trader.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

import com.mgkkmg.trader.api.common.filter.HttpRequestCacheFilter;
import com.mgkkmg.trader.core.infra.alarm.slack.RequestStorage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class FilterConfig {

	private final RequestStorage requestStorage;

	@Bean
	public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilterRegistrationBean() {
		FilterRegistrationBean<ForwardedHeaderFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new ForwardedHeaderFilter());
		registrationBean.setOrder(Integer.MAX_VALUE - 1);

		return registrationBean;
	}

	@Bean
	public FilterRegistrationBean<HttpRequestCacheFilter> httpRequestCacheFilterRegistrationBean() {
		FilterRegistrationBean<HttpRequestCacheFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new HttpRequestCacheFilter(requestStorage));
		registrationBean.setOrder(Integer.MAX_VALUE);

		return registrationBean;
	}
}
