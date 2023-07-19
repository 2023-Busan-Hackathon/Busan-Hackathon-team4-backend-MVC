package busanhackathon.team4.heart.controller;

import busanhackathon.team4.heart.service.HeartService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HeartController {

    private final HeartService heartService;
    @PostMapping("/heart/{postId}")
    public ResponseEntity<?> enrollHeart(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable("postId") Long postId) {
        Long heartId = heartService.enrollHeart(principalDetails.getLoginId(), postId);
        return ResponseEntity.ok("찜완료");
    }
}
