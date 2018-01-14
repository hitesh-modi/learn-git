package com.moditraders.services;

import java.util.Collection;
import java.util.Date;

import com.moditraders.models.InvoiceReportModel;

public interface IInvoiceReportService {

	Collection<InvoiceReportModel> getInvoices(Date fromDate, Date toDate);

}
