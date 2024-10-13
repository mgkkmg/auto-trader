package com.mgkkmg.trader.core.infra.handler;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.lang.NonNull;
import org.springframework.web.client.HttpServerErrorException;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackAlarmClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private final SlackAlarmClient slackAlarmClient;

	@Override
	public void handleUncaughtException(@NonNull Throwable throwable, @NonNull Method method, @NonNull Object... obj) {
		if (throwable instanceof HttpServerErrorException) {
			slackAlarmClient.sendErrorAlarm((Exception) throwable);
		}

		log.error("AsyncExceptionHandler: {}", throwable.getMessage());
	}
}
