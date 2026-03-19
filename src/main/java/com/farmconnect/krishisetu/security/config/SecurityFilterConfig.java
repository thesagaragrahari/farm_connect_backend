package com.farmconnect.krishisetu.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.farmconnect.krishisetu.security.filter.AuthTokenFilter;
import com.farmconnect.krishisetu.security.handler.CustomAccessDeniedHandler;
import com.farmconnect.krishisetu.security.handler.CustomAuthenticationEntryPoint;
  
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityFilterConfig {

    private final AuthTokenFilter authTokenFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityFilterConfig(
            AuthTokenFilter authTokenFilter,
            CustomAccessDeniedHandler accessDeniedHandler,
            CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.authTokenFilter = authTokenFilter;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth/**",
                        "/api/public/**",
                        "/actuator/health",
                        "/actuator/info",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated());
        
        http.addFilterBefore(authTokenFilter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
