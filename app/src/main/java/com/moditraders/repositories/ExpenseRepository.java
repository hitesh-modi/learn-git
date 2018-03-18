package com.moditraders.repositories;

import com.moditraders.entities.ExpensesEntity;
import com.moditraders.entities.Productdetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("expenseRepository")
public interface ExpenseRepository extends CrudRepository<ExpensesEntity, Long>{
	

}
