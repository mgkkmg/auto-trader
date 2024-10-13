package com.mgkkmg.trader.core.infra.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.mgkkmg.trader.core.infra.alarm.slack.SlackAlarmClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@Aspect
public class SlackAlarmAspect {

	private final SlackAlarmClient slackAlarmClient;

	private static final String SLACK_ALARM_FORMAT = "[SlackAlarm] %s";

	@Before("@annotation(com.mgkkmg.trader.common.annotation.SlackErrorAlarm)")
	public void exceptionAlarm(JoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();

		// 매개변수에서 Exception 객체를 찾는다.
		Exception e = null;
		for (Object arg : args) {
			if (arg instanceof Exception) {
				e = (Exception) arg;
				break;
			}
		}

		if (ObjectUtils.isEmpty(e)) {
			log.warn(String.format(SLACK_ALARM_FORMAT, "argument is not Exception"));
			return;
		}

		slackAlarmClient.sendErrorAlarm(e);
	}
}
