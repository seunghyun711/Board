package com.board.BoardService.member;

import com.board.BoardService.exception.BusinessLogicException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

// spring 사용하지 않고, Junit의 Mockito 기능을 사용하기 위해 선언하는 애너테이션
@ExtendWith(MockitoExtension.class)
public class MemberServiceMockTest {
    @Mock // memberRepository를 Mock 객체로 생성
    private MemberRepository memberRepository;

    @InjectMocks // 이 애너테이션이 붙은 필드에 mock 객체로 생성한 memberRepository 객체를 주입
    private MemberService memberService; // memberService 객체는 주입 받은 memberRepository Mock 객체를 포함

    @DisplayName("createMember() 테스트")
    @Test
    public void createMemberTest(){
        // given
        Member member = new Member("hong", "seunghyun", "hong@naver.com", "sa123456789");
        given(memberRepository.findByEmail(Mockito.anyString()))
                .willReturn(Optional.of(member));

        // when / then
        // 6
        assertThrows(BusinessLogicException.class, () -> memberService.createMember(member));
    }

}
