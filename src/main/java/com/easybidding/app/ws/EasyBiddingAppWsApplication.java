package com.easybidding.app.ws;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.easybidding.app.ws.config.AppProperties;
import com.easybidding.app.ws.properties.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
    FileStorageProperties.class
})
public class EasyBiddingAppWsApplication {

	private static final Logger logger = LoggerFactory.getLogger(EasyBiddingAppWsApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(EasyBiddingAppWsApplication.class, args);
		logger.info("Application Started");
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public EasyBiddingApplicationContext applicationContext() {
		return new EasyBiddingApplicationContext();
	}

	@Bean
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		mapper.getConfiguration().setAmbiguityIgnored(true);
		mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
		return mapper;
	}
	
}
