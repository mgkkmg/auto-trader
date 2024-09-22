package com.mgkkmg.trader.api.apis.coin.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.dto.AccountDto;
import com.mgkkmg.trader.api.apis.coin.usecase.AccountUseCase;
import com.mgkkmg.trader.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AccountController {

	private final AccountUseCase accountUseCase;

	@GetMapping("/api/v1/accounts")
	public SuccessResponse<List<AccountDto>> getAccountsInfo() {
		return SuccessResponse.of(accountUseCase.getAccountsInfo());
	}
}
