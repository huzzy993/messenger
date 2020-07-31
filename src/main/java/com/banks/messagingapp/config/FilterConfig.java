package com.banks.messagingapp.config;

import com.banks.messagingapp.model.CurrentUserDetails;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import static com.banks.messagingapp.controller.MessageController.MESSAGES_BASE_PATH;

@Component
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<UserIdFilter> loggingFilter(UserIdFilter userIdFilter) {
		FilterRegistrationBean<UserIdFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(userIdFilter);
		bean.addUrlPatterns(MESSAGES_BASE_PATH + "/*");

		return bean;
	}

	@Bean
	@RequestScope
	public CurrentUserDetails currentUser() {
		return new CurrentUserDetails();
	}
}
