package com.kshrd.krorya.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.krorya.exception.CustomUnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthEntrypoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/problem+json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> data = new HashMap<>();
        data.put("type", "https://localhost/8080/unauthorized");
        data.put("title", "Unauthorized");
        data.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        data.put("detail", authException.getMessage());
        data.put("instance", request.getRequestURI());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getOutputStream().println(objectMapper.writeValueAsString(data));

    }
}
