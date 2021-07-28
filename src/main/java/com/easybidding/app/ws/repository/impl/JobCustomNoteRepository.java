package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.JobCustomNoteEntity;
import com.easybidding.app.ws.io.entity.JobCustomNoteEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface JobCustomNoteRepository extends BaseRepository<JobCustomNoteEntity, String> {

	void deleteByIdIn(List<String> ids);

	@Query("SELECT jcn FROM JobCustomNoteEntity jcn LEFT JOIN jcn.job j LEFT JOIN jcn.account a WHERE j.id = :jobId AND a.id = :accountId AND jcn.status = :status")
	List<JobCustomNoteEntity> findAllNotesByJobAndAccount(@Param("jobId") String jobId, @Param("accountId") String accountId, @Param("status") Status status);

}
