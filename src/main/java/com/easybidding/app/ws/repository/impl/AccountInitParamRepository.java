package com.easybidding.app.ws.repository.impl;

import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.AccountInitParamEntity;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface AccountInitParamRepository extends BaseRepository<AccountInitParamEntity, String> {

	AccountInitParamEntity findByAccountId(String accountId);

	AccountInitParamEntity findByParamKey(String paramKey);
}
