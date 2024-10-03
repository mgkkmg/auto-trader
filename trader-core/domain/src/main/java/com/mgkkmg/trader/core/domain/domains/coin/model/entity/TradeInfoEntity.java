package com.mgkkmg.trader.core.domain.domains.coin.model.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.mgkkmg.trader.core.domain.domains.coin.model.dto.TradeInfoDto;
import com.mgkkmg.trader.core.domain.domains.coin.model.enums.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "trade_info")
@Entity
public class TradeInfoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "decision", length = 10)
	private String decision;

	@Column(name = "percentage")
	private Integer percentage;

	@Column(name = "reason", length = 4000)
	private String reason;

	@Column(name = "krw_balance")
	private String krwBalance;

	@Column(name = "btc_balance")
	private String btcBalance;

	@Column(name = "btc_avg_buy_price")
	private String btcAvgBuyPrice;

	@Column(name = "btc_krw_price")
	private Double btcKrwPrice;

	@Column(name = "relection", length = 4000)
	private String reflection;

	@Enumerated(EnumType.STRING)
	@Column(name = "order_status")
	private OrderStatus orderStatus;

	private TradeInfoEntity(TradeInfoDto tradeInfoDto) {
		this.decision = Objects.requireNonNull(tradeInfoDto.decision(), "decision은 null이 될 수 없습니다.");
		this.percentage = tradeInfoDto.percentage();
		this.reason = tradeInfoDto.reason();
		this.btcBalance = tradeInfoDto.btcBalance();
		this.krwBalance = tradeInfoDto.krwBalance();
		this.btcAvgBuyPrice = tradeInfoDto.btcAvgBuyPrice();
		this.btcKrwPrice = tradeInfoDto.btcKrwPrice();
		this.createdAt = LocalDateTime.now();
		this.reflection = tradeInfoDto.reflection();
		this.orderStatus = tradeInfoDto.orderStatus();
	}

	public static TradeInfoEntity of(TradeInfoDto tradeInfoDto) {
		return new TradeInfoEntity(tradeInfoDto);
	}
}
