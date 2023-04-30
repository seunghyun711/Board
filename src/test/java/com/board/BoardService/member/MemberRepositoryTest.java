package com.board.BoardService.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // MemberRepository의 기능을 정상적으로 사용하기 위한 Configuration을 스프링이 자동으로 추가 - @Transactional이 있다.
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원 정보 저장 테스트")
    @Test
    public void saveMemberTest(){
        // given
        Member member = new Member();
        member.setEmail("hong@naver.com");
        member.setNickname("hong");
        member.setName("seunghyun");
        member.setPassword("a123456789");

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertNotNull(savedMember);
        assertTrue(member.getEmail().equals(savedMember.getEmail()));
        assertTrue(member.getNickname().equals(savedMember.getNickname()));
        assertTrue(member.getName().equals(savedMember.getName()));
        assertTrue(member.getPassword().equals(savedMember.getPassword()));
    }

}