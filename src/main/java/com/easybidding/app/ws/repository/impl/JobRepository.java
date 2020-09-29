package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.JobEntity;
import com.easybidding.app.ws.io.entity.JobEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface JobRepository extends BaseRepository<JobEntity, String> {

	void deleteByIdIn(List<String> ids);

	@Query("SELECT j FROM JobEntity j JOIN j.accounts a WHERE a.id = :accountId")
	public List<JobEntity> findAllJobsByAccount(@Param("accountId") String accountId);

	@Query("SELECT j FROM JobEntity j JOIN j.accounts a WHERE a.id = :accountId AND j.status = :status")
	public List<JobEntity> findAllJobsByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status);

	@Query("SELECT j FROM JobEntity j JOIN j.accounts a WHERE a.id = :accountId")
	public Page<JobEntity> findJobsByAccount(@Param("accountId") String accountId, Pageable pageable);

	@Query("SELECT j FROM JobEntity j JOIN j.accounts a WHERE a.id = :accountId AND j.status = :status")
	public Page<JobEntity> findJobsByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status, Pageable pageable);

}
