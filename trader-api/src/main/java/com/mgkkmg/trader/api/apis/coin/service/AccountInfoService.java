package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.common.response.coin.AccountResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;
import com.mgkkmg.trader.core.infra.config.UpbitConfig;
import com.mgkkmg.trader.core.infra.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountInfoService {

	private final UpbitApiClient upbitApiClient;
	private final JwtTokenProvider jwtTokenProvider;
	private final UpbitConfig upbitConfig;

	public List<AccountDto> getAccounts() {
		List<AccountResponse> accountResponses = upbitApiClient.getAccounts(MediaType.APPLICATION_JSON_VALUE,
			jwtTokenProvider.createToken(upbitConfig.getSecretKey(), upbitConfig.getAccessKey()));

		return accountResponses.stream()
			.map(AccountDto::from)
			.toList();
	}
}
