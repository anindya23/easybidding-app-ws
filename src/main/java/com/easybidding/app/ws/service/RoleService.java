package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import com.easybidding.app.ws.shared.dto.RoleDto;

public interface RoleService {

	RoleDto getById(String id);

	RoleDto getByRoleCode(String roleCode);

	List<RoleDto> getAllRoles();

	List<RoleDto> getAllRolesByAccountAndStatus(String accountId, String status);

	List<RoleDto> getAllRolesByAccount(String accountId);

	Map<String, Object> getRoles(int page, int size);

	Map<String, Object> getRolesByAccountAndStatus(String accountId, String status, int page, int size);

	Map<String, Object> getRolesByAccount(String accountId, int page, int size);

	RoleDto save(RoleDto dto);

	void delete(String id);

	void batchSave(List<RoleDto> dtos);

	void batchDelete(List<String> ids);

}
