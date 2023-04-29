package com.board.BoardService.auth.handler;

import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

// 로그인 인증 성공 시 로그 기록
@Slf4j
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String email = authentication.getName();
        String accessToken = response.getHeader("Authorization");
        String memberId = response.getHeader("memberId");
        String nickname = response.getHeader("nickname");

        System.out.println("User " + email + " successfully authenticated");
        try(PrintWriter writer = response.getWriter()){
            JsonObject json = new JsonObject();
            json.addProperty("memberId", memberId);
            json.addProperty("nickname", nickname);
            json.addProperty("AccessToken", accessToken);

            response.setStatus(HttpStatus.ACCEPTED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());

            writer.write(json.toString());
        }
        log.info("# Authenticated successfully!");
    }
}
