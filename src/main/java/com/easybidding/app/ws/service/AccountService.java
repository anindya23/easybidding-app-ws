package com.easybidding.app.ws.service;

import java.util.List;
import java.util.Map;

import com.easybidding.app.ws.io.entity.AccountEntity.Status;
import com.easybidding.app.ws.io.entity.CountryEntity;
import com.easybidding.app.ws.io.entity.CountyEntity;
import com.easybidding.app.ws.io.entity.StateEntity;
import com.easybidding.app.ws.shared.dto.AccountDto;

public interface AccountService {

	AccountDto getAccountById(String id);

	AccountDto getAccountByEmail(String email);

	List<AccountDto> searchAccountByName(String term);

	List<AccountDto> getAllAccounts();

	List<AccountDto> getAllAccountsByStatus(Status status);

	List<AccountDto> getAllAccountsByCounty(CountyEntity county);

	List<AccountDto> getAllAccountsByState(StateEntity state);

	List<AccountDto> getAllAccountsByCountry(CountryEntity country);

	Map<String, Object> getAllAccounts(int page, int size);

	Map<String, Object> getAccountsByStatus(Status status, int page, int size);

	Map<String, Object> getAccountsByCounty(CountyEntity county, int page, int size);

	Map<String, Object> getAccountsByState(StateEntity state, int page, int size);

	Map<String, Object> getAccountsByCountry(CountryEntity country, int page, int size);

	AccountDto save(AccountDto dto);

	void softDelete(String id);

	void batchDelete(List<String> ids);

	void batchSave(List<AccountDto> dtos);

}
