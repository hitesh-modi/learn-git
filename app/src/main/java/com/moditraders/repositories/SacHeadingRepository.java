package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.SacHeadingMaster;

@Repository("sacHeadingRepo")
public interface SacHeadingRepository extends CrudRepository<SacHeadingMaster, String>{
	
}
