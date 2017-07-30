package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.ConsigneeDetail;
import com.moditraders.entities.CustomerDetail;

@Repository("consigneeRepository")
public interface ConsigneeRepository extends CrudRepository<ConsigneeDetail, Long>{
	
}
