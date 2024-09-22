package com.mgkkmg.trader.api.apis.scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.usecase.ExecuteUseCase;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CoinTraderExecute {

	private final ExecuteUseCase executeUseCase;

	@GetMapping("/execute")
	public void execute() {
		executeUseCase.execute();
	}
}
