//package com.lumen.employeeRelations.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.io.IOException;
//
//import static org.mockito.Mockito.*;
//
//class ApiKeyAuthFilterTest {
//
//    private ApiKeyAuthFilter filter;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private FilterChain chain;
//
//    @BeforeEach
//    void setUp() {
//        filter = new ApiKeyAuthFilter();
//        ReflectionTestUtils.setField(filter, "apiKey", "my-secure-key");
//
//        request = mock(HttpServletRequest.class);
//        response = mock(HttpServletResponse.class);
//        chain = mock(FilterChain.class);
//    }
//
//    @Test
//    void testDoFilter_withValidApiKey() throws IOException, ServletException {
//        when(request.getHeader("x-api-key")).thenReturn("my-secure-key");
//
//        filter.doFilter(request, response, chain);
//
//        verify(chain, times(1)).doFilter(request, response);
//        verify(response, never()).sendError(anyInt(), anyString());
//    }
//
//    @Test
//    void testDoFilter_withInvalidApiKey() throws IOException, ServletException {
//        when(request.getHeader("x-api-key")).thenReturn("invalid-key");
//
//        filter.doFilter(request, response, chain);
//
//        verify(response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing API key.");
//        verify(chain, never()).doFilter(request, response);
//    }
//}