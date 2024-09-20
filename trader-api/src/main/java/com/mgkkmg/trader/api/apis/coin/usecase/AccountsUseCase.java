package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.service.AccountsInfoService;
import com.mgkkmg.trader.common.annotation.UseCase;
import com.mgkkmg.trader.common.response.AccountsResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class AccountsUseCase {

	private final AccountsInfoService accountsInfoService;

	public List<AccountsResponse> getAccountsInfo() {
		return accountsInfoService.getAccounts();
	}
}
