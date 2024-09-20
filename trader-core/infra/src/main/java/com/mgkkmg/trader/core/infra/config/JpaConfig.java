package com.mgkkmg.trader.core.infra.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mgkkmg.trader.core.infra.common.importer.AutoTraderConfig;

@EnableTransactionManagement
@EntityScan("com.mgkkmg.trader")
@EnableJpaRepositories("com.mgkkmg.trader")
public class JpaConfig implements AutoTraderConfig {
}