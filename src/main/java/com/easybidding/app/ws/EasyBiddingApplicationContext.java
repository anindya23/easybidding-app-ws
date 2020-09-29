package com.easybidding.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class EasyBiddingApplicationContext implements ApplicationContextAware {

	private static ApplicationContext CONTEXT;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		CONTEXT = context;
	}

	public static <T extends Object> T getBean(Class<T> beanClass) {
        return CONTEXT.getBean(beanClass);
    }

	public static Object getBean(String beanName) {
		return CONTEXT.getBean(beanName);
	}

}
