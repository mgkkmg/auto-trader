package com.mgkkmg.trader.api.apis.coin.usecase;

import java.util.List;

import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.api.apis.coin.service.AccountInfoService;
import com.mgkkmg.trader.common.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
public class AccountUseCase {

	private final AccountInfoService accountInfoService;

	public List<AccountDto> getAccountsInfo() {
		return accountInfoService.getAccounts();
	}
}
