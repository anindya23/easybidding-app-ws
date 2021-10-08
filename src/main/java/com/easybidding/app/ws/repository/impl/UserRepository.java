package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.UserEntity;
import com.easybidding.app.ws.io.entity.UserEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {

	void deleteByIdIn(List<String> ids);
	
	UserEntity findByEmail(String email);

//	UserEntity findByAccountId(String accountId);

	@Query("SELECT u FROM UserEntity u JOIN u.account a WHERE a.id = :accountId AND u.status = 'ACTIVE'")
	public List<UserEntity> findAllUsersByAccount(@Param("accountId") String accountId);

	@Query("SELECT u FROM UserEntity u JOIN u.account a WHERE a.id = :accountId AND u.status = :status")
	public List<UserEntity> findAllUsersByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status);

	@Query("SELECT u FROM UserEntity u JOIN u.account a WHERE a.id = :accountId AND u.status = 'ACTIVE'")
	public Page<UserEntity> findUsersByAccount(@Param("accountId") String accountId, Pageable pageable);

	@Query("SELECT u FROM UserEntity u JOIN u.account a WHERE a.id = :accountId AND u.status = :status")
	public Page<UserEntity> findUsersByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status, Pageable pageable);

}
