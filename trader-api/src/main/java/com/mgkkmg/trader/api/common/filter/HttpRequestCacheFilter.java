package com.mgkkmg.trader.api.common.filter;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.mgkkmg.trader.core.infra.alarm.slack.RequestStorage;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HttpRequestCacheFilter extends OncePerRequestFilter {

	private final RequestStorage requestStorage;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		// ContentCachingRequestWrapper: HttpServletRequest의 내용을 캐싱하여 여러번 읽을 수 있게 해줌
		ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
		requestStorage.set(wrappedRequest);

		filterChain.doFilter(wrappedRequest, response);
	}
}
