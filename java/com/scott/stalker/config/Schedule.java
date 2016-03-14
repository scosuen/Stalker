package com.scott.stalker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.scott.stalker.bll.UserBll;

@Configuration
@EnableScheduling
@PropertySources({ @PropertySource(value = "classpath:chat_config.properties", ignoreResourceNotFound = true),
		@PropertySource(value = "classpath:xx.properties", ignoreResourceNotFound = true) })
public class Schedule {
	
	
	@Autowired
	private UserBll userBll;
	
	@Scheduled(fixedDelay = 60 * 1000)
	public void updateSessionUpdateTime() {
		long msce = 30*60*1000;
		userBll.validateTimeoutUsers(msce);
	}
}
