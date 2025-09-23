package com.example.lat.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter implements Filter {

    @Value("${app.auth.token}")
    private String expectedToken;

    @Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    String token = req.getHeader("Authorization");

    // 🔍 Add these lines at the top
    System.out.println("AuthFilter triggered");
    System.out.println("Received token: " + token);
    System.out.println("Expected token: Bearer " + expectedToken);

    if (token == null || !token.equals("Bearer " + expectedToken)) {
        ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        return;
    }

    chain.doFilter(request, response);
}
}