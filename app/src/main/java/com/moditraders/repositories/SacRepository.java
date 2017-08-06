package com.moditraders.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.SacAccountingcodeGroupMap;
import com.moditraders.entities.SacGroupHeadinMap;

@Repository("sacRepo")
public interface SacRepository extends CrudRepository<SacAccountingcodeGroupMap, String>{
	
	@Query("select s from  SacAccountingcodeGroupMap s where sacGroupMaster.groupId = :groupId")
	public Collection<SacAccountingcodeGroupMap> getSacByGroupId(@Param("groupId") String groupId);
	
}
