package com.moditraders.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.Productdetail;

@Repository("productRepository")
public interface ProductRepository extends CrudRepository<Productdetail, Long>{
	
	List<Productdetail> findByProductId(int productId);
	
}
