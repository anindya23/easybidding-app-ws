package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.JobCustomFieldEntity;
import com.easybidding.app.ws.io.entity.JobCustomFieldEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface JobCustomFieldRepository extends BaseRepository<JobCustomFieldEntity, String> {

	void deleteByIdIn(List<String> ids);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf JOIN jcf.job j WHERE j.id = :jobId AND jcf.status = :status")
	List<JobCustomFieldEntity> findAllFieldsByJobAndStatus(@Param("jobId") String jobId, @Param("status") Status status);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf JOIN jcf.job j WHERE j.id = :jobId AND jcf.status = :status")
	Page<JobCustomFieldEntity> findFieldsByJobAndStatus(@Param("jobId") String jobId, @Param("status") Status status,
			Pageable pageable);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf WHERE jcf.id IN :ids AND jcf.status = 'ACTIVE'")
	public List<JobCustomFieldEntity> findAllFieldsByIds(@Param("ids") List<String> ids);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf WHERE jcf.id IN :ids AND jcf.status = 'ACTIVE'")
	public Page<JobCustomFieldEntity> findFieldsByIds(@Param("ids") List<String> ids, Pageable pageable);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf JOIN jcf.job j WHERE j.id = :jobId AND jcf.status = 'ACTIVE'")
	public List<JobCustomFieldEntity> findAllFieldsByJob(@Param("jobId") String jobId);

	@Query("SELECT jcf FROM JobCustomFieldEntity jcf JOIN jcf.job j WHERE j.id = :jobId AND jcf.status = 'ACTIVE'")
	public Page<JobCustomFieldEntity> findFieldsByJob(@Param("jobId") String jobId, Pageable pageable);
	
	@Query("SELECT jcf FROM JobCustomFieldEntity jcf LEFT JOIN jcf.job j LEFT JOIN jcf.account a WHERE j.id = :jobId AND a.id = :accountId AND jcf.status = 'ACTIVE'")
	List<JobCustomFieldEntity> findAllFieldsByJobAndAccount(@Param("jobId") String jobId, @Param("accountId") String accountId);
	
	@Query("SELECT jcf FROM JobCustomFieldEntity jcf LEFT JOIN jcf.account a WHERE a.id = :accountId AND jcf.status = 'ACTIVE'")
	List<JobCustomFieldEntity> findAllFieldsByAccount(@Param("accountId") String accountId);

}
