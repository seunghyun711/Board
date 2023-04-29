package com.board.BoardService.auth.userdetails;

import com.board.BoardService.auth.utils.MemberAuthorityUtils;
import com.board.BoardService.member.Member;
import com.board.BoardService.member.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

// db에서 조회한 User의 인증 정보를 기반으로 인증 처리하는 클래스
@Component
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final MemberAuthorityUtils authorityUtils;

    public MemberDetailsService(MemberRepository memberRepository,
                                MemberAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new UsernameNotFoundException(username));

        return new MemberDetails(findMember);
    }

    private final class MemberDetails extends Member implements UserDetails {
        public MemberDetails(Member member) {
            setMemberId(member.getMemberId());
            setNickname(member.getNickname());
            setName(member.getName());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }


}
