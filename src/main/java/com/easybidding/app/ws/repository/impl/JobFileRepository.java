package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.JobFileEntity;
import com.easybidding.app.ws.io.entity.JobFileEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface JobFileRepository extends BaseRepository<JobFileEntity, String> {

	void deleteByIdIn(List<String> ids);

	@Query("SELECT jf FROM JobFileEntity jf JOIN jf.job j WHERE j.id = :jobId AND jf.status = :status")
	List<JobFileEntity> findAllFilesByJobAndStatus(@Param("jobId") String jobId, @Param("status") Status status);

	@Query("SELECT jf FROM JobFileEntity jf JOIN jf.job j WHERE j.id = :jobId AND jf.status = :status")
	Page<JobFileEntity> findFilesByJobAndStatus(@Param("jobId") String jobId, @Param("status") Status status,
			Pageable pageable);

	@Query("SELECT jf FROM JobFileEntity jf WHERE jf.id IN :ids")
	public List<JobFileEntity> findAllFilesByIds(@Param("ids") List<String> ids);

	@Query("SELECT jf FROM JobFileEntity jf WHERE jf.id IN :ids")
	public Page<JobFileEntity> findFilesByIds(@Param("ids") List<String> ids, Pageable pageable);

	@Query("SELECT jf FROM JobFileEntity jf JOIN jf.job j WHERE j.id = :jobId")
	public List<JobFileEntity> findAllFilesByJob(@Param("jobId") String jobId);

	@Query("SELECT jf FROM JobFileEntity jf JOIN jf.job j WHERE j.id = :jobId")
	public Page<JobFileEntity> findFilesByJob(@Param("jobId") String jobId, Pageable pageable);
	
	@Query("SELECT jf FROM JobFileEntity jf LEFT JOIN jf.job j LEFT JOIN jf.account a WHERE j.id = :jobId AND a.id = :accountId")
	List<JobFileEntity> findAllFilesByJobAndAccount(@Param("jobId") String jobId, @Param("accountId") String accountId);

}
