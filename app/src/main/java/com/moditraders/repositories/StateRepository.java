package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.State;

@Repository("stateRepo")
public interface StateRepository extends CrudRepository<State, String>{
}
