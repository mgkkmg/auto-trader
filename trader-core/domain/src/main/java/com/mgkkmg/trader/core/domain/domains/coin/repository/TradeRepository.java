package com.mgkkmg.trader.core.domain.domains.coin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgkkmg.trader.core.domain.domains.coin.model.entity.TradeInfoEntity;

@Repository
public interface TradeRepository extends JpaRepository<TradeInfoEntity, Long> {

	@Query("SELECT ti FROM TradeInfoEntity ti WHERE ti.createdAt >= :lastDaysAgo")
	List<TradeInfoEntity> findAllFromLastDays(@Param("lastDaysAgo") LocalDateTime lastDaysAgo);
}
