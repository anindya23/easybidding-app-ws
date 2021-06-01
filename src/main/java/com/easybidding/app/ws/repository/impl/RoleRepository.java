package com.easybidding.app.ws.repository.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.RoleEntity;
import com.easybidding.app.ws.io.entity.RoleEntity.Status;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface RoleRepository extends BaseRepository<RoleEntity, String> {

	RoleEntity findByAccountId(String accountId);

	RoleEntity findByRoleCode(String roleCode);

	List<RoleEntity> findByStatus(Status status);

	void deleteByIdIn(List<String> ids);

	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id = :accountId AND r.roleCode = :roleCode")
	public RoleEntity findRoleByAccount(@Param("accountId") String accountId, @Param("roleCode") String roleCode);
	
	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id = :accountId")
	public List<RoleEntity> findAllRolesByAccount(@Param("accountId") String accountId);

	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id = :accountId AND r.status = :status")
	public List<RoleEntity> findAllRolesByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status);

	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id = :accountId")
	public Page<RoleEntity> findRolesByAccount(@Param("accountId") String accountId, Pageable pageable);

	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id = :accountId AND r.status = :status")
	public Page<RoleEntity> findRolesByAccountAndStatus(@Param("accountId") String accountId,
			@Param("status") Status status, Pageable pageable);
	
	@Query("SELECT r FROM RoleEntity r JOIN r.account a WHERE a.id IS NULL AND r.roleCode = :roleCode")
	public RoleEntity findDefaultRoleByRoleCode(@Param("roleCode") String roleCode);

}
