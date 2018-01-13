package com.moditraders.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import com.itextpdf.text.DocumentException;
import com.moditraders.models.Invoice;

public interface IInvoiceService {

	public String createInvoice(final Invoice invoice);

	public File createInvoicePDF(String invoiceId, String userId) throws DocumentException, MalformedURLException, IOException;
	
}
