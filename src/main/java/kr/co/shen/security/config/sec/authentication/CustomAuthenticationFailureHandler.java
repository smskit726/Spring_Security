package kr.co.shen.security.config.sec.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
 * 인증 실패시 (로그인 실패) handler
 * 로그인 실패 시 처리할 내용 작성, 로그인 실패 횟수 update 등...
 * */
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("::: onAuthenticationFailure :::");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> map = new HashMap<>();
        String message = "";
        if(exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "아이디 또는 비밀번호를 확인하세요.";
        } else {
            // TODO something..
            message = "에러발생";
        }

        map.put("message", message);
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
