package com.farmconnect.krishisetu.security.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        // Log the exception for server-side debugging
        System.err.println("Unauthorized error: " + authException.getMessage());
        
        // 1. Set Response Properties
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401
        
        // 2. Construct the Custom Message
        String message = String.format(
            "{\"status\": 401, \"error\": \"Unauthorized\", \"path\": \"%s\", \"message\": \"Authentication failed. You must provide valid credentials (e.g., a JWT) to access this resource.\"}",
            request.getRequestURI()
        );

        // 3. Write the message to the response
        response.getWriter().write(message);
    }
}