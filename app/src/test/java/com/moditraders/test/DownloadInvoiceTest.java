package com.moditraders.test;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.itextpdf.text.DocumentException;
import com.moditraders.models.Invoice;
import com.moditraders.services.IInvoiceService;
import com.moditraders.test.config.TestConfig;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class ,loader=AnnotationConfigContextLoader.class)
public class DownloadInvoiceTest {

	@Resource(name="invoiceService")
	private IInvoiceService invoiceServcie;
	
	@Test
	public void test() throws MalformedURLException, DocumentException, IOException {
		invoiceServcie.createInvoicePDF("27", "er.hiteshmodi@gmail.com");
	}

}
