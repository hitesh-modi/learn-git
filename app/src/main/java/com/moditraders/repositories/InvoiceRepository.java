package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.CustomerDetail;
import com.moditraders.entities.Invoicedetail;

@Repository("invoiceRepo")
public interface InvoiceRepository extends CrudRepository<Invoicedetail, String>{
	
}
