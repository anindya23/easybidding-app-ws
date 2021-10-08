package com.easybidding.app.ws.repository.impl;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.JobAccountEntity;
import com.easybidding.app.ws.io.entity.JobAccountEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface JobAccountRepository extends BaseRepository<JobAccountEntity, String> {

	@Query("SELECT ja FROM JobAccountEntity ja JOIN ja.account a WHERE a.id IN :ids AND ja.status = 'ACTIVE'")
	public Set<JobAccountEntity> findByAccountIds(@Param("ids") List<String> ids);

	@Query("SELECT ja FROM JobAccountEntity ja JOIN ja.job j WHERE j.id IN :ids AND ja.status = 'ACTIVE'")
	public Set<JobAccountEntity> findByJobIds(@Param("ids") List<String> ids);

	@Query("SELECT ja FROM JobAccountEntity ja JOIN FETCH ja.account a WHERE a.id = :accountId AND ja.status = :status")
	public Set<JobAccountEntity> findAllJobsByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status);

	void deleteByIdIn(List<String> ids);
}
