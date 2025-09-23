package com.example.lat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Autowired
private AuthFilter authFilter;

@Bean
public FilterRegistrationBean<AuthFilter> registerAuthFilter() {
    FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(authFilter);
    registrationBean.addUrlPatterns("/students", "/students/*", "/attendance", "/attendance/*");
    return registrationBean;
}
}