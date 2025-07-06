//package com.lumen.employeeRelations.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//
//@Configuration
//public class FilterConfig {
//
//    @Autowired
//    private ApiKeyAuthFilter apiKeyAuthFilter;
//
//    @Bean
//    public FilterRegistrationBean<ApiKeyAuthFilter> filterRegistrationBean() {
//        FilterRegistrationBean<ApiKeyAuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(apiKeyAuthFilter);
//        registrationBean.addUrlPatterns("/employees/*", "/projects/*");
//        return registrationBean;
//    }
//}