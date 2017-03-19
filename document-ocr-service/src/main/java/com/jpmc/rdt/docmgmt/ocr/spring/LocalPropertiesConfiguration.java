package com.jpmc.rdt.docmgmt.ocr.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.jpmc.rdt.docmgmt.spring.DevProfile;
import com.jpmc.rdt.docmgmt.spring.LocalProfile;

@Configuration
@PropertySource({ "classpath:application.properties.local" })
@DevProfile
@LocalProfile
public class LocalPropertiesConfiguration {
}
