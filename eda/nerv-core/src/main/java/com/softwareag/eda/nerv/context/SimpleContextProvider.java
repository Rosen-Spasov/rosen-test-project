package com.softwareag.eda.nerv.context;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultCamelContextNameStrategy;
import org.apache.camel.spi.CamelContextNameStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleContextProvider implements ContextProvider {

	public static final String NAME_TEMPLATE = "com.softwareag.eda.nerv.default.context";

	private static final Logger logger = LoggerFactory.getLogger(SimpleContextProvider.class);

	private final CamelContext context;

	public SimpleContextProvider() {
		this.context = createContext();
		if (logger.isInfoEnabled()) {
			logger.info("Created Camel context: " + context.getName());
		}
	}

	public SimpleContextProvider(CamelContext context) {
		this.context = context;
	}

	private CamelContext createContext() {
		CamelContext context = new DefaultCamelContext();
		CamelContextNameStrategy nameStrategy = new DefaultCamelContextNameStrategy(NAME_TEMPLATE);
		context.setNameStrategy(nameStrategy);
		context.getShutdownStrategy().setShutdownNowOnTimeout(true);
		return context;
	}

	@Override
	public CamelContext context() {
		return context;
	}

}
