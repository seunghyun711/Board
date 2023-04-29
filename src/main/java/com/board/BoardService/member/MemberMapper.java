package com.board.BoardService.member;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    // MemberDto.Post -> Member
    Member memberPostDtoToMember(MemberDto.Post memberPostDto);

    // Member -> MemberDto.Response
    MemberDto.Response memberToMemberResponseDto(Member member);

    // MemberDto.Patch -> Member
    Member memberPatchDtoToMember(MemberDto.Patch memberPatchDto);

}
