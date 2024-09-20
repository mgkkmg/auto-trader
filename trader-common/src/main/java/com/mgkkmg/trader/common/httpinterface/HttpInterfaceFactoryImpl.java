package com.mgkkmg.trader.common.httpinterface;

import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

public class HttpInterfaceFactoryImpl implements HttpInterfaceFactory {
	@Override
	public <T> T create(Class<T> clientClass, RestClient restClient) {
		return HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
			.build()
			.createClient(clientClass);
	}
}
