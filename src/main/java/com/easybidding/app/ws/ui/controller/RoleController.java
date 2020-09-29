package com.easybidding.app.ws.ui.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.repository.impl.AccountRepository;
import com.easybidding.app.ws.repository.impl.RoleRepository;
import com.easybidding.app.ws.service.RoleService;
import com.easybidding.app.ws.shared.Utils;
import com.easybidding.app.ws.shared.dto.RoleDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

	@Autowired
	RoleService roleService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	Utils utils;

	@GetMapping("/role/{id}")
	public RoleDto getRole(@PathVariable String id) {
		return roleService.getById(id);
	}

	@GetMapping("/account/{accountId}")
	public List<RoleDto> getAllRolesByAccount(@PathVariable String accountId) {
		return roleService.getAllRolesByAccount(accountId);
	}

	@GetMapping("/account/{accountId}/status/{status}")
	public List<RoleDto> getAllRolesByAccountAndStatus(@PathVariable String accountId, @PathVariable String status) {
		return roleService.getAllRolesByAccountAndStatus(accountId, status);
	}

	@GetMapping("/account/{accountId}/page/{page}/size/{size}")
	public Map<String, Object> getRolesByAccount(@PathVariable String accountId, @PathVariable Integer page,
			@PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return roleService.getRolesByAccount(accountId, page, size);
	}

	/*
	 * Refactoring 1. Default Page and Size should not be hardcoded
	 */
	@GetMapping("/account/{accountId}/status/{status}/page/{page}/size/{size}")
	public Map<String, Object> getRolesByAccountAndStatus(@PathVariable String accountId, @PathVariable String status,
			@PathVariable Integer page, @PathVariable Integer size) {
		page = page == null ? 0 : page;
		size = size == null ? 3 : size;

		return roleService.getRolesByAccountAndStatus(accountId, status, page, size);
	}

	@PostMapping("/role")
	public RoleDto createRole(@Valid @RequestBody RoleDto request) throws ParseException {
		return roleService.save(request);
	}

	@PutMapping("/role")
	public RoleDto updateRole(@Valid @RequestBody RoleDto request) throws ParseException {
		return roleService.save(request);
	}

	@DeleteMapping("/role/{id}")
	public OperationStatusModel deleteRole(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		roleService.delete(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchSave(@Valid @RequestBody List<RoleDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		roleService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<RoleDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (RoleDto req : request) {
			ids.add(req.getId());
		}

		roleService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
