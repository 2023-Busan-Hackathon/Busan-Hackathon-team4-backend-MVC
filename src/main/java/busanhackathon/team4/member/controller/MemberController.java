package busanhackathon.team4.member.controller;

import busanhackathon.team4.member.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {
    private final MemberService memberService;

    /**
     * 회원가입 폼
     */
    @GetMapping("/join")
    public String joinForm() {
        return "member/join";
    }

    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(JoinFormDto joinFormDto) {
        Long loginID = memberService.saveMember(joinFormDto);
        log.info("회원가입");
        return "redirect:/login";
    }

    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm() {
        log.info("로그인");
        return "member/login";
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinFormDto {
        private String loginId;
        private String password;
        private String nickname;
    }

}