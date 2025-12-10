package com.farmconnect.krishisetu.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.farmconnect.krishisetu.security.jwt.AuthTokenFilter;



// ... other imports ...
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ... existing Beans (passwordEncoder, authenticationManager) ...
    private final AuthTokenFilter authTokenFilter;
    
    public SecurityConfig(AuthTokenFilter authTokenFilter) { // <--- Injection point
        this.authTokenFilter = authTokenFilter;
    }



    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .headers(headers -> headers
            .frameOptions(frame -> frame.disable()) // <-- Needed for Swagger
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <-- Swagger Fix
            .requestMatchers(
                "/api/auth/**", 
                "/api/public/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
            ).permitAll()
            // ... inside authorizeHttpRequests ...
            .requestMatchers("/api/dashboard").hasAnyRole("USER", "FARMER", "WORKER") 
// ...
            .anyRequest().authenticated()
        );

    http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}



    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //      http
    //         .csrf(csrf -> csrf.disable())
    //         .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    //         .sessionManagement(session -> 
    //             session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //         )
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // <-- IMPORTANT
    //             .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
    //             .requestMatchers(
    //                     "/v3/api-docs/**",
    //                     "/swagger-ui/**",
    //                     "/swagger-ui.html"
    //             ).permitAll()
    //             .anyRequest().authenticated()
    //         );

    //     http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

    //     return http.build();
    // }


    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    //     http.csrf(csrf -> csrf.disable())
    //         .cors(cors -> cors.configurationSource(corsConfigurationSource())) // <-- ADD CORS HERE
    //         .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    //         .authorizeHttpRequests(auth -> auth
    //             .requestMatchers("/api/auth/**", "/api/public/**").permitAll()
    //             .requestMatchers(
    //     "/v3/api-docs/**",
    //                 "/swagger-ui/**",
    //                 "/swagger-ui.html"
    //                 ).permitAll()
    //             .anyRequest().authenticated()
    //         );

    //     http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    //     return http.build();
    // }

    /**
     * Define the CORS policy to allow requests from your front-end domain.
     */


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); // or your domain
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    //     CorsConfiguration configuration = new CorsConfiguration();
        
    //     // 1. Specify the origins (front-end domains) allowed to access your API
    //     configuration.addAllowedOrigin("http://localhost:3000"); // Example: React/Vue/Angular dev server
    //     configuration.addAllowedOrigin("https://your.frontend.app"); // Production domain

    //     // 2. Specify allowed HTTP methods (GET, POST, PUT, DELETE, etc.)
    //     configuration.addAllowedMethod("*"); 

    //     // 3. Specify allowed headers (e.g., Authorization header for JWT)
    //     configuration.addAllowedHeader("*"); 

    //     // 4. Allow sending cookies and authentication headers (if needed)
    //     configuration.setAllowCredentials(true); 

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     // Apply this configuration to all endpoints: /**
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}