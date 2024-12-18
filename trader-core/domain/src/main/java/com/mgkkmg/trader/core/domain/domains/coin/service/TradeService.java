package com.mgkkmg.trader.core.domain.domains.coin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.mgkkmg.trader.common.annotation.DomainService;
import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;
import com.mgkkmg.trader.core.domain.domains.coin.model.entity.TradeInfoEntity;
import com.mgkkmg.trader.core.domain.domains.coin.model.enums.OrderStatus;
import com.mgkkmg.trader.core.domain.domains.coin.repository.TradeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class TradeService {

	private final TradeRepository tradeRepository;

	@Transactional
	public void createTradeInfo(TradeInfoDto tradeInfoDto) {
		tradeRepository.save(TradeInfoEntity.of(tradeInfoDto));
	}

	public List<TradeInfoDto> getTradeInfoFromLastDays(final int day) {
		LocalDateTime lastDaysAgo = LocalDateTime.now().minusDays(day);

		return tradeRepository.findTradesFromLastDays(lastDaysAgo, OrderStatus.SKIP).stream()
			.map(TradeInfoDto::fromEntity)
			.toList();
	}

	public List<TradeInfoDto> getAllTradesOrderedByCreatedAtDesc() {
		return tradeRepository.findAllByOrderStatusIsNotOrderByCreatedAtDesc(OrderStatus.SKIP).stream()
			.map(TradeInfoDto::fromEntity)
			.toList();
	}
}
