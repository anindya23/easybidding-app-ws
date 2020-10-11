package com.easybidding.app.ws.repository.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.AccountEntity;
import com.easybidding.app.ws.io.entity.AccountEntity.Status;
import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface AccountRepository extends BaseRepository<AccountEntity, String> {

	AccountEntity findByEmail(String email);

	List<AccountEntity> findByStatus(Status status);

	List<AccountEntity> findByCounty(CountyEntity county);

	List<AccountEntity> findByState(StateEntity state);

	List<AccountEntity> findByCountry(CountryEntity country);

	List<AccountEntity> findByAccountNameContainingIgnoreCase(String term);

	Page<AccountEntity> findByStatus(Status status, Pageable pageable);

	Page<AccountEntity> findByCounty(CountyEntity county, Pageable pageable);

	Page<AccountEntity> findByState(StateEntity state, Pageable pageable);

	Page<AccountEntity> findByCountry(CountryEntity county, Pageable pageable);

	@Query("SELECT a FROM AccountEntity a WHERE a.id IN :ids")
	public Set<AccountEntity> findAccountsByIds(@Param("ids") List<String> ids);

	@Query("SELECT a FROM AccountEntity a WHERE a.id IN :ids")
	public List<AccountEntity> findAccountsByIds(@Param("ids") List<String> ids, Pageable pageable);

	void deleteByIdIn(List<String> ids);
}
