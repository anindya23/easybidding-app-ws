package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.io.entity.CountyEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface CountyRepository extends BaseRepository<CountyEntity, String> {

	void deleteByIdIn(List<String> ids);

	List<CountyEntity> findByStatus(Status status);

	Page<CountyEntity> findByStatus(Status status, Pageable pageable);

	@Query("SELECT c FROM CountyEntity c JOIN c.country cn Join c.state s WHERE s.stateCode = :stateCode AND cn.countryCode = :countryCode")
	public List<CountyEntity> findByStateAndCountry(@Param("stateCode") String stateCode,
			@Param("countryCode") String countryCode);

	@Query("SELECT c FROM CountyEntity c JOIN c.country cn Join c.state s WHERE "
			+ "c.countyCode = :countyCode AND s.stateCode = :stateCode AND cn.countryCode = :countryCode")
	public CountyEntity findByCountyCode(@Param("countyCode") String countyCode, @Param("stateCode") String stateCode,
			@Param("countryCode") String countryCode);

	@Query("SELECT c FROM CountyEntity c WHERE c.id IN :ids")
	public List<CountyEntity> findCountiesByIds(@Param("ids") List<String> ids);

	@Query("SELECT c FROM CountyEntity c WHERE c.id IN :ids")
	public Page<CountyEntity> findCountiesByIds(@Param("ids") List<String> ids, Pageable pageable);

}
