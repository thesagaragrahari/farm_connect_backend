package com.farmconnect.krishisetu.security.jwt;

// com.farmconnect.krishisetu.security.jwt.AuthTokenFilter

// ... existing imports ...

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.farmconnect.krishisetu.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthTokenFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        // --- REMOVE THIS BLOCK ---
        // String path = request.getRequestURI();
        // // Skip JWT validation for public routes
        // if (path.startsWith("/api/auth/") || 
        //     path.startsWith("/v3/api-docs") ||
        //     path.startsWith("/swagger-ui")) {

        //     filterChain.doFilter(request, response);
        //     return;
        // }
        // -------------------------

        // ADD: Skip JWT parsing and validation for CORS preflight (OPTIONS) requests.
        // Spring Security already permits this in the config, but we skip the token logic here for cleanliness.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);

            if (jwt != null && jwtUtil.validateJwtToken(jwt)) {
                String username = jwtUtil.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Log exception: e.g., token expired, or user not found, but let the filter chain continue
            // Spring Security will then deny access if the path is protected and authentication failed.
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Extract the token after "Bearer "
        }
        return null;
    }
}