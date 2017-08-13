package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.SectionMaster;

@Repository("hsnSectionRepo")
public interface HSNSectionRepository extends CrudRepository<SectionMaster, String>{
	
}
