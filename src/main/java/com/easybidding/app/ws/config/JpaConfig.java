package com.easybidding.app.ws.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.easybidding.app.ws.repository.impl.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(basePackages = "com.easybidding.app.ws.repository", repositoryBaseClass = BaseRepositoryImpl.class)
public class JpaConfig {
}