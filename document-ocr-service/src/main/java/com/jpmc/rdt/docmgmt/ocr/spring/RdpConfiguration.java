package com.jpmc.rdt.docmgmt.ocr.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.jpmc.rdt.docmgmt.spring.RdpProfile;

@Configuration
@RdpProfile
@PropertySource({ "classpath:application.properties" })
public class RdpConfiguration {

}
