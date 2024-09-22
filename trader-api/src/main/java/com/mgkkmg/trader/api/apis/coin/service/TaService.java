package com.mgkkmg.trader.api.apis.coin.service;

import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

@Service
public class TaService {

	public BarSeries createBarSeries() {
		BarSeries series = new BaseBarSeries();

		return series;
	}
}
