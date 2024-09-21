package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.service.AccountInfoService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.response.AccountResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class AccountUseCase {

	private final AccountInfoService accountInfoService;

	public List<AccountResponse> getAccountsInfo() {
		return accountInfoService.getAccounts();
	}
}
