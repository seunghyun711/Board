package com.board.BoardService.auth;

import com.board.BoardService.auth.filter.JwtAuthenticationFilter;
import com.board.BoardService.auth.filter.JwtVerificationFilter;
import com.board.BoardService.auth.handler.MemberAccessDeniedHandler;
import com.board.BoardService.auth.handler.MemberAuthenticationEntryPoint;
import com.board.BoardService.auth.handler.MemberAuthenticationFailureHandler;
import com.board.BoardService.auth.handler.MemberAuthenticationSuccessHandler;
import com.board.BoardService.auth.jwt.JwtTokenizer;
import com.board.BoardService.auth.utils.MemberAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {
    private final MemberAuthorityUtils authorityUtils;
    private final JwtTokenizer jwtTokenizer;

    public SecurityConfiguration(MemberAuthorityUtils authorityUtils, JwtTokenizer jwtTokenizer) {
        this.authorityUtils = authorityUtils;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable()
                .cors(withDefaults()) // corsConfigurationSource라는 이름의 Bean 사용
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 생성하지 않음
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint()) // MemberAuthenticationEntryPoint 추가
                .accessDeniedHandler(new MemberAccessDeniedHandler()) // MemberAccessDeniedHandler 추가
                .and()
                .apply(new CustomFilterConfigurer()) // CustomFilterConfigurer() 추가
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/members").permitAll()
                        .antMatchers(HttpMethod.DELETE, "/members/delete/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/questions/**").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/questions/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/questions/**").hasRole("USER")
                        .antMatchers(HttpMethod.GET, "/answers/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST, "/answers").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH, "/answers/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE, "/answers/**").hasRole("USER")
                        .anyRequest().permitAll());

        return http.build();
    }

    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // CORS 정책 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("*")); // 모든 출처에 대해 HTTP 통신 허용
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","PATCH","DELETE")); // 해당 HTTP 메서드에 대한 통신 허용

        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL에 CORS 정책 적용

        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            // getSharedObject()로 SecurityConfigurer 간 공유되는 객체 획득
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
            jwtAuthenticationFilter.setFilterProcessesUrl("/members/login");

            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler()); // 로그인 인증 성공 시 처리
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler()); // 로그인 인증 실패 시 처리

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils);

            builder
                    .addFilter(jwtAuthenticationFilter) // JwtAuthenticationFilter를 Spring Security Filter Chain에 추가
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);

        }
    }
}
