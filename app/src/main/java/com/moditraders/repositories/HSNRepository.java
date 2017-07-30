package com.moditraders.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.HSN;

@Repository("hsnRepo")
public interface HSNRepository extends CrudRepository<HSN, Long>{
	@Query("select h from HSN h where UPPER(h.hsnDesc) like CONCAT('%',:keyword,'%')")
	public List<HSN> getHSNForCodes(@Param("keyword") String keyword);
}
