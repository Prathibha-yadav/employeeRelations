//package com.lumen.employeeRelations.config;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class ApiKeyAuthFilter extends GenericFilter {
//
//    @Value("${employee.api.key}")
//    private String apiKey;
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) resp;
//
//        String headerKey = request.getHeader("x-api-key");
//
//        if (apiKey.equals(headerKey)) {
//            chain.doFilter(request, response);
//        } else {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing API key.");
//        }
//    }
//}