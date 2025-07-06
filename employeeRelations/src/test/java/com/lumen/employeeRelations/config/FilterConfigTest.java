//package com.lumen.employeeRelations.config;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FilterConfigTest {
//
//    @Test
//    void testFilterRegistrationBean() {
//        ApiKeyAuthFilter mockFilter = mock(ApiKeyAuthFilter.class);
//        FilterConfig config = new FilterConfig();
//
//        // Use reflection to inject the private field
//        ReflectionTestUtils.setField(config, "apiKeyAuthFilter", mockFilter);
//
//        FilterRegistrationBean<ApiKeyAuthFilter> bean = config.filterRegistrationBean();
//
//        assertNotNull(bean);
//        assertEquals(mockFilter, bean.getFilter());
//        assertTrue(bean.getUrlPatterns().contains("/api/employees/*"));
//        assertTrue(bean.getUrlPatterns().contains("/api/projects/*"));
//    }
//}