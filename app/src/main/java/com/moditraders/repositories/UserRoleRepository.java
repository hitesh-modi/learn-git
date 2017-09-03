package com.moditraders.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.UserRole;
import com.moditraders.entities.UserRoleKey;

@Repository("userRoleRepo")
public interface UserRoleRepository extends CrudRepository<UserRole, UserRoleKey>{
}
