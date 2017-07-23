package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.CustomerDetail;

@Repository("customerRepository")
public interface CustomerRepository extends CrudRepository<CustomerDetail, Long>{
	
}
