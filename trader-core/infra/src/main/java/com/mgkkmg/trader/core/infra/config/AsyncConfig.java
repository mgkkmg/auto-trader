package com.mgkkmg.trader.core.infra.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackAlarmClient;
import com.mgkkmg.trader.core.infra.handler.AsyncExceptionHandler;
import com.mgkkmg.trader.core.infra.importer.AutoTraderConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableAsync
@Configuration
public class AsyncConfig implements AutoTraderConfig {

	private final SlackAlarmClient slackAlarmClient;

	@Bean(name = "asyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setThreadNamePrefix("async-thread-");
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(100);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		executor.initialize();

		return executor;
	}

	@Bean
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler(slackAlarmClient);
	}
}
