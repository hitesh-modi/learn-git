package com.moditraders.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
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
import com.moditraders.services.IInvoiceReportService;
import com.moditraders.services.IInvoiceService;
import com.moditraders.test.config.TestConfig;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfig.class ,loader=AnnotationConfigContextLoader.class)
public class InvoiceTest {

	@Resource(name="invoiceService")
	private IInvoiceService invoiceServcie;
	
	@Resource(name="invoiceReportService")
	private IInvoiceReportService invoiceReportService;
	
	@Test
	public void downloadInvoice() throws MalformedURLException, DocumentException, IOException {
		invoiceServcie.createInvoicePDF("27", "er.hiteshmodi@gmail.com");
	}
	
	@Test
	public void getInvoiceReport() {
		invoiceReportService.getInvoices(DateUtils.addDays(Calendar.getInstance().getTime(), -20), Calendar.getInstance().getTime());
	}

}
