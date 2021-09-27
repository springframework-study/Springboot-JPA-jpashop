package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @PutMapping("/api/members/{id}")
    public UpdateMemberResponse updateMember(@PathVariable("id") Long id,
        @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @GetMapping("/api/members")
    public Result members() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
            .map(m -> new MemberDto(m.getName()))
            .collect(Collectors.toList());

        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    class MemberDto {
        private String name;
    }
}
