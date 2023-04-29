package com.board.BoardService.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Getter
public class MemberDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Post{
        @Valid
        @NotBlank(message = "닉네임을 입력하세요.")
        private String nickname;

        @Valid
        @NotBlank(message = "이름을 입력하세요.")
        private String name;

        @Valid
        @Email
        @NotBlank(message = "이메일을 입력하세요.")
        private String email;

        @Valid
        @NotBlank(message = "패스워드를 입력하세요.")
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patch{
        @Positive
        private long memberId;
        @Valid
        private String nickname;

        @Valid
        private String password;
    }

    @Getter
    @AllArgsConstructor
    public static class Response{
        @Positive
        private long memberId;
        private String nickname;
        private String name;
        private String email;
    }
}
