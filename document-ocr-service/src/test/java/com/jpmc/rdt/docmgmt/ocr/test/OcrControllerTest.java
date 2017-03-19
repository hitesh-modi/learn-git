package com.jpmc.rdt.docmgmt.ocr.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.jpmc.rdt.docmgmt.ocr.spring.OcrSpringConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:META-INF/spring/document-digitization-service-spring-test-context.xml" })
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class, classes = { OcrSpringConfiguration.class })
@ActiveProfiles(profiles = "local" )
@WebAppConfiguration
public class OcrControllerTest {

}
