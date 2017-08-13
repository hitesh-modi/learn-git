package com.moditraders.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.moditraders.entities.ChapterSectionMap;

@Repository("hsnChapterRepo")
public interface HSNChapterRepository extends CrudRepository<ChapterSectionMap, String>{
	@Query("select chapter from ChapterSectionMap chapter where sectionMaster.sectionId = :sectionId ")
	public Collection<ChapterSectionMap> getChaptersBySectionId(@Param("sectionId") String sectionId);
}
