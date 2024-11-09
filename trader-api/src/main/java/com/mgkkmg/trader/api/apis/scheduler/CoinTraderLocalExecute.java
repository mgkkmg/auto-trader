package com.mgkkmg.trader.api.apis.scheduler;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mgkkmg.trader.api.apis.coin.usecase.ExecuteUseCase;

import lombok.RequiredArgsConstructor;

@Profile({"local"})
@RequiredArgsConstructor
@RestController
public class CoinTraderLocalExecute {

	private final ExecuteUseCase executeUseCase;

	@GetMapping("/execute")
	public void execute() {
		executeUseCase.execute();
	}
}
