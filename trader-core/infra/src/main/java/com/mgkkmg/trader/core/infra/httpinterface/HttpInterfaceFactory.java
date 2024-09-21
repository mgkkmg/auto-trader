package com.mgkkmg.trader.core.infra.httpinterface;

import org.springframework.web.client.RestClient;

public interface HttpInterfaceFactory {
	<T> T create(Class<T> clientClass, RestClient restClient);
}
