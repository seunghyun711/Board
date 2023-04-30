package com.board.BoardService.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest // 스프링 부트 기반의 애플리케이션 테스트 위한 Application Context 생성
@AutoConfigureMockMvc // Controller 테스트를 위한 애플리케이션의 자동 구성 작업 진행
class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @DisplayName("회원 등록 테스트")
    @Test
    public void postMemberTest() throws Exception {
        // given
        MemberDto.Post post = new MemberDto.Post("hong", "seunghyun", "hong@naver.com", "sa123456789");
        String content = gson.toJson(post); // 2

        // when
        ResultActions actions =
                mockMvc.perform(
                        MockMvcRequestBuilders.post("/members/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        String location = actions.andReturn().getResponse().getHeader("Location");

        // then
        actions
                .andExpect(status().isCreated());
 //               .andExpect(header().string("Location", is(startsWith("/members/signup"))));
    }


}