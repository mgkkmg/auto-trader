package com.mgkkmg.trader.common.client;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.mgkkmg.trader.common.response.AccountsResponse;
import com.mgkkmg.trader.common.response.CandleMinuteResponse;

public interface UpbitApiClient {
	@GetExchange("https://api.upbit.com/v1/accounts")
	List<AccountsResponse> getAccounts(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken
	);

	// @PostExchange("https://api.upbit.com/v1/orders")
	// List<OrdersResponse> orders(
	// 	@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
	// 	@RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken,
	// 	@RequestBody OrdersRequest ordersRequest
	// );

	@GetExchange("https://api.upbit.com/v1/candles/minutes/{unit}")
	List<CandleMinuteResponse> getCandlesMinute(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@PathVariable(value="unit") Integer unit,
		@RequestParam(value="params") MultiValueMap<String, Object> params
	);
}
