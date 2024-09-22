package com.mgkkmg.trader.core.infra.client;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import com.mgkkmg.trader.common.response.coin.AccountResponse;
import com.mgkkmg.trader.common.response.coin.CandleDayResponse;
import com.mgkkmg.trader.common.response.coin.CandleMinuteResponse;
import com.mgkkmg.trader.common.response.coin.OrderbookResponse;

public interface UpbitApiClient {
	@GetExchange("https://api.upbit.com/v1/accounts")
	List<AccountResponse> getAccounts(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken
	);

	@GetExchange("https://api.upbit.com/v1/orderbook")
	List<OrderbookResponse> getOrderbook(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestParam(value="params") MultiValueMap<String, Object> params
	);

	@GetExchange("https://api.upbit.com/v1/candles/minutes/{unit}")
	List<CandleMinuteResponse> getCandlesMinutes(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@PathVariable(value="unit") Integer unit,
		@RequestParam(value="params") MultiValueMap<String, Object> params
	);

	@GetExchange("https://api.upbit.com/v1/candles/days")
	List<CandleDayResponse> getCandlesDays(
		@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
		@RequestParam(value="params") MultiValueMap<String, Object> params
	);

	// @PostExchange("https://api.upbit.com/v1/orders")
	// List<OrdersResponse> orders(
	// 	@RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
	// 	@RequestHeader(HttpHeaders.AUTHORIZATION) String authenticationToken,
	// 	@RequestBody OrdersRequest ordersRequest
	// );
}
