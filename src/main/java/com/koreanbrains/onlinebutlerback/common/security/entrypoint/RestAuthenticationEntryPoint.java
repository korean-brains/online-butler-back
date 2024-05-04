package com.koreanbrains.onlinebutlerback.common.security.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koreanbrains.onlinebutlerback.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        ErrorCode errorCode = ErrorCode.valueOf(exception);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getStatus());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> result = new HashMap<>();
        result.put("code", errorCode.getCode());
        result.put("message", errorCode.getMessage());

        objectMapper.writeValue(response.getWriter(), result);
    }
}
