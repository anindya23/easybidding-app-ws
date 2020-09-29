package com.easybidding.app.ws.repository.impl;

import org.springframework.stereotype.Repository;

import com.easybidding.app.ws.io.entity.SystemInitParamEntity;
import com.easybidding.app.ws.repository.BaseRepository;

@Repository
public interface SystemInitParamRepository extends BaseRepository<SystemInitParamEntity, String> {

	SystemInitParamEntity findByParamKey(String paramKey);
}
