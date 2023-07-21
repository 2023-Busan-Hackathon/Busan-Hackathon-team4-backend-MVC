package busanhackathon.team4.member.controller;

import busanhackathon.team4.member.dto.MyInfoDto;
import busanhackathon.team4.member.service.MemberService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@Slf4j
public class MemberController {
    private final MemberService memberService;


    /**
     * 회원가입
     */
    @PostMapping("/join")
    public String join(JoinFormDto joinFormDto) {
        Long loginID = memberService.saveMember(joinFormDto);
        if(loginID == null) {
            return "redirect:/login?errorMessage=true";
        }
        log.info("회원가입");
        return "redirect:/login";
    }

    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "errorMessage", required = false) String errorMessage,
                            Model model) {
        if(errorMessage != null) {
            model.addAttribute("errorMessage", "이미 가입된 회원입니다.");
        }
        log.info("로그인");
        return "member/login";
    }

    /**
     * 마이페이지 이동
     */
    @GetMapping("myPage")
    public String myPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        MyInfoDto myInfoDto = memberService.myPageInfo(principalDetails.getLoginId());
        model.addAttribute("myInfoDto", myInfoDto);
        return "member/myPage";
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