package com.prgrms.offer.core.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.offer.common.ApiResponse;
import com.prgrms.offer.common.message.ResponseMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse responseBody = ApiResponse.of(ResponseMessage.LOGIN_FAIL);

        response.getWriter()
                .write(objectMapper.writeValueAsString(responseBody));
    }
}
