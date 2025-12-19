package com.farmconnect.krishisetu.security.service;



import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException, ServletException {

        // 1. Get Authentication Details
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 2. Set Response Properties
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // HTTP 403

        // 3. Construct the Custom Message
        String message;
        if (auth != null) {
            // Check if the user is logged in
            String username = auth.getName();
            String currentRoles = auth.getAuthorities().toString();

            // NOTE: You cannot easily extract the *required* role from the exception here.
            // The message must be generalized or based on your known API structure.
            message = String.format(
                "{\"status\": 403, \"error\": \"Forbidden\", \"path\": \"%s\", \"message\": \"Access Denied for user %s. Your current roles (%s) are insufficient to access this resource.\"}",
                request.getRequestURI(), username, currentRoles
            );
        } else {
            // Unauthenticated attempt (though typically handled by AuthenticationEntryPoint)
            message = "{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"Access Denied. You must be authenticated to access this resource.\"}";
        }

        // 4. Write the message to the response
        response.getWriter().write(message);
    }
}