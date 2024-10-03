package com.mgkkmg.trader.api.apis.coin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.mgkkmg.trader.common.response.coin.OrderResponse;
import com.mgkkmg.trader.core.infra.client.UpbitApiClient;
import com.mgkkmg.trader.core.infra.config.UpbitConfig;
import com.mgkkmg.trader.core.infra.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderExecuteService {

	private final UpbitApiClient upbitApiClient;
	private final JwtTokenProvider jwtTokenProvider;
	private final UpbitConfig upbitConfig;

	private static final double MINIMUM_ORDER_AMOUNT = 5000.0;

	public OrderResponse executeBuyOrder(String market, double availableBalance, int investRate) {
		if (availableBalance > 0) {
			double buyAmount = calculateBuyAmount(availableBalance, investRate);
			log.info("buyAmount: {}", buyAmount);
			if (buyAmount < MINIMUM_ORDER_AMOUNT) {
				log.info("Calculated buy amount {} is less than minimum order amount {}. Adjusting to minimum.", buyAmount, MINIMUM_ORDER_AMOUNT);
				buyAmount = MINIMUM_ORDER_AMOUNT;
			}

			if (buyAmount >= availableBalance) {
				log.info("Insufficient balance for buy order. Available: {}, Required: {}", availableBalance, buyAmount);
				return null;
			}

			Map<String, String> params = new HashMap<>();
			params.put("market", market);
			params.put("side", "bid");
			params.put("price", String.valueOf((long) buyAmount));
			params.put("ord_type", "price");

			return placeOrder(params);
		} else {
			log.info("매수 주문 잔액 부족");
			return null;
		}
	}

	public OrderResponse executeSellOrder(String market, double coinBalance, double currentPrice, int investRate) {
		if (coinBalance > 0) {
			double sellAmount = calculateSellAmount(coinBalance, investRate);

			if (sellAmount * currentPrice < MINIMUM_ORDER_AMOUNT) {
				log.info("Calculated sell amount {} is less than minimum order amount {}. Cancelling order.", sellAmount * currentPrice, MINIMUM_ORDER_AMOUNT);
				return null;
			}

			Map<String, String> params = new HashMap<>();
			params.put("market", market);
			params.put("side", "ask");
			params.put("volume", String.format("%.8f", sellAmount));
			params.put("ord_type", "market");

			return placeOrder(params);
		} else {
			log.info("매도 주문에 대한 코인 잔액 부족");
			return null;
		}
	}

	private OrderResponse placeOrder(Map<String, String> params) {
		List<String> queryElements = new ArrayList<>();
		for (Map.Entry<String, String> entity : params.entrySet()) {
			queryElements.add(entity.getKey() + "=" + entity.getValue());
		}

		return upbitApiClient.placeOrder(
			MediaType.APPLICATION_JSON_VALUE,
			jwtTokenProvider.createToken(queryElements, upbitConfig.getSecretKey(), upbitConfig.getAccessKey()),
			params
		);
	}

	private double calculateBuyAmount(double availableBalance, int investRate) {
		// 수수료(0.05%)
		double fee = 0.9995;

		// 사용 가능 잔액의 매수 비율 계산
		double targetAmount = availableBalance * (investRate / 100.0);

		// 수수료를 고려하여 실제 주문 금액 계산
		return targetAmount * fee;
	}

	private double calculateSellAmount(double coinBalance, int investRate) {
		// 사용 가능 잔액의 매도 비율 계산
		return coinBalance * (investRate / 100.0);
	}
}
