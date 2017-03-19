package com.jpmc.rdt.docmgmt.ocr.spring;

import javax.servlet.ServletContextEvent;

import org.apache.commons.lang.SystemUtils;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.web.context.ContextLoaderListener;

public class OCRContextLoaderListener extends ContextLoaderListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		if (SystemUtils.IS_OS_WINDOWS) {
			System.out.println("Configured for local");
			System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "local");
		} else {
			System.out.println("Configured for rdp");
			System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "rdp");
		}
		super.contextInitialized(event);

	}
}
