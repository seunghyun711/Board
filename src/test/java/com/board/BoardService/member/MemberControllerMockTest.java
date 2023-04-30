package com.board.BoardService.member;

import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerMockTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean // 해당 필드의 빈에 대한 Mock 객체를 생성하여 필드에 주입
    private MemberService memberService; // MemberService 빈에 대한 Mock 객체를 생성해서 memberService 필드에 주입

    @Autowired
    private MemberMapper mapper;

    @DisplayName("회원 등록 테스트")
    @Test
    void postMemberTest() throws Exception {
        // given
        MemberDto.Post post = new MemberDto.Post("hong", "seunghyun", "hong@naver.com", "sa123456789");
        Member member = mapper.memberPostDtoToMember(post);
        member.setMemberId(1L);

        // given()은 Mock 객체가 특정 값을 리턴하는 동작을 지정할 때 사용 -> Mock 객체인 memberService 객체로 createMember() 메서드 호출하도록 정의
        given(memberService.createMember(Mockito.any(Member.class))) // createMember()의 타입은 Member. 따라서 Member.class로 선언
                .willReturn(member); // stub 데이터 리턴

        String content = gson.toJson(post);

        // when
        ResultActions actions =
                mockMvc.perform(
                        post("/members/signup")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated());
    }
}
