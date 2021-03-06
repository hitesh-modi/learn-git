package com.moditraders.repositories;

import java.sql.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.InvoiceNumberDetail;

@Repository("invoiceNumberRepo")
public interface InvoiceNumberRepo extends CrudRepository<InvoiceNumberDetail, Long>{
	@Query("select max(sequenceNo) from InvoiceNumberDetail where invoiceDate = ?")
	public Integer getInvoiceSequenceNumber(Date date);
}
