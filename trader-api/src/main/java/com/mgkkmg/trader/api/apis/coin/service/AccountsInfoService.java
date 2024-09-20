package com.mgkkmg.trader.api.apis.coin.service;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mgkkmg.trader.common.client.UpbitApiClient;
import com.mgkkmg.trader.common.response.AccountsResponse;
import com.mgkkmg.trader.core.infra.common.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AccountsInfoService {

	private final UpbitApiClient upbitApiClient;
	private final JwtTokenProvider jwtTokenProvider;

	public List<AccountsResponse> getAccounts() {
		return upbitApiClient.getAccounts(MediaType.APPLICATION_JSON_VALUE, jwtTokenProvider.createToken());
	}
}
