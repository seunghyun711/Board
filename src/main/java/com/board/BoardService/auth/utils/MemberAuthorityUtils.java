package com.board.BoardService.auth.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

// ROLE 기반 사용자의 권한 생성
@Component
public class MemberAuthorityUtils {
    // 관리자 이메일
    @Value("${mail.address.admin}")
    private String adminMailAddress;

    // 관리자 권한
    private final List<String> ADMIN_ROLES_STRING = List.of("ADMIN", "USER");

    // 일반 사용자 권한
    private final List<String> USER_ROLES_STRING = List.of("USER");


    // db에 저장된 Role을 기반으로 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        return authorities;
    }

    // DB 저장
    public List<String> createRoles(String email) {
        if (email.equals(adminMailAddress)) {
            return ADMIN_ROLES_STRING;
        }
        return USER_ROLES_STRING;
    }
}
