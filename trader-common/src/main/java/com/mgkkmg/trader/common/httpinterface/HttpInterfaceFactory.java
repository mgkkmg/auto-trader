package com.mgkkmg.trader.common.httpinterface;

import org.springframework.web.client.RestClient;

public interface HttpInterfaceFactory {
	<T> T create(Class<T> clientClass, RestClient restClient);
}
