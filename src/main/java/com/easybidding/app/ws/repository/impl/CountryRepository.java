package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountryEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

public interface CountryRepository extends BaseRepository<CountryEntity, String> {

	CountryEntity findByCountryCode(String countryCode);

	List<CountryEntity> findByStatus(Status status);

	Page<CountryEntity> findByStatus(Status status, Pageable pageable);

	@Query("SELECT a FROM CountryEntity a WHERE a.id IN :ids")
	public List<CountryEntity> findCountriesByIds(@Param("ids") List<String> ids);

	@Query("SELECT a FROM CountryEntity a WHERE a.id IN :ids")
	public Page<CountryEntity> findCountriesByIds(@Param("ids") List<String> ids, Pageable pageable);

	void deleteByIdIn(List<String> ids);
}
