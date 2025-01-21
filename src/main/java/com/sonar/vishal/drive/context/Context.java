package com.sonar.vishal.drive.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Context implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static synchronized <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static synchronized <T> T getBean(Class<T> requiredType, Object... args) {
        return applicationContext.getBean(requiredType, args);
    }

    public static synchronized <T> Map<String, T> getBeansOfType(Class<T> requiredType) {
        return applicationContext.getBeansOfType(requiredType);
    }

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Context.applicationContext = applicationContext;
    }
}
