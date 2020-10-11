package com.easybidding.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easybidding.app.ws.service.AccountService;
import com.easybidding.app.ws.shared.dto.AccountDto;
import com.easybidding.app.ws.ui.model.response.OperationStatusModel;
import com.easybidding.app.ws.ui.model.response.RequestOperationStatus;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

	@Autowired
	AccountService accountService;

	@PutMapping
	public AccountDto updateAccount(@Valid @RequestBody AccountDto request) {
		return accountService.save(request);
	}

	@DeleteMapping("/account/{id}")
	public OperationStatusModel deleteAccount(@PathVariable(value = "id") String id) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.DELETE.name());

		accountService.delete(id);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@PostMapping
	public OperationStatusModel batchSave(@Valid @RequestBody List<AccountDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHINSERT.name());

		accountService.batchSave(request);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

	@DeleteMapping
	public OperationStatusModel batchDelete(@Valid @RequestBody List<AccountDto> request) {
		OperationStatusModel response = new OperationStatusModel();
		response.setOperationName(RequestOperationName.BATCHDELETE.name());

		List<String> ids = new ArrayList<String>();

		for (AccountDto req : request) {
			ids.add(req.getId());
		}

		accountService.batchDelete(ids);

		response.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return response;
	}

}
