package com.jpmc.rdt.docmgmt.ocr.test.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.jpmc.rdt.docmgmt.ocr.spring.OcrSpringConfiguration;

@Configuration
@ComponentScan(basePackages = { "com.jpmc.rdt.docmgmt.ocr", "com.jpmc.rdt.docmgmt.database.persistence, com.jpmc.rdt.docmgmt.ta"})
@Import({OcrSpringConfiguration.class})
public class OcrTestSpringConfiguration {

}
