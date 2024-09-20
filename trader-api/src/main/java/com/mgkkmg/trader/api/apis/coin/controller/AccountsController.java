package com.mgkkmg.trader.api.apis.coin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.usecase.AccountsUseCase;
import com.mgkkmg.trader.common.response.AccountsResponse;
import com.mgkkmg.trader.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AccountsController {

	private final AccountsUseCase accountsUseCase;

	@GetMapping("/api/v1/accounts")
	public SuccessResponse<List<AccountsResponse>> getAccountsInfo() {
		return SuccessResponse.of(accountsUseCase.getAccountsInfo());
	}
}
