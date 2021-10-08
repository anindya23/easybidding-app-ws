package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.io.entity.StateEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface StateRepository extends BaseRepository<StateEntity, String> {

	void deleteByIdIn(List<String> ids);

	List<StateEntity> findByStatus(Status status);

	Page<StateEntity> findByStatus(Status status, Pageable pageable);

	@Query("SELECT s FROM StateEntity s JOIN s.country c WHERE c.countryCode = :countryCode AND s.status = 'ACTIVE' ORDER BY s.stateName ASC")
	public List<StateEntity> findByCountry(@Param("countryCode") String countryCode);

	@Query("SELECT s FROM StateEntity s JOIN s.country c WHERE s.stateCode = :stateCode AND c.countryCode = :countryCode AND s.status = 'ACTIVE'")
	public StateEntity findByStateCode(@Param("stateCode") String stateCode, @Param("countryCode") String countryCode);

	@Query("SELECT s FROM StateEntity s WHERE s.id IN :ids AND s.status = 'ACTIVE'")
	public List<StateEntity> findStatesByIds(@Param("ids") List<String> ids);

	@Query("SELECT s FROM StateEntity s WHERE s.id IN :ids AND s.status = 'ACTIVE'")
	public Page<StateEntity> findStatesByIds(@Param("ids") List<String> ids, Pageable pageable);

}
