package busanhackathon.team4.heart.controller;

import busanhackathon.team4.heart.service.HeartService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HeartController {

    private final HeartService heartService;
    @PostMapping("/heart")
    public ResponseEntity<?> enrollHeart(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody HeartDto heartDto) {
        heartService.enrollHeart(principalDetails.getLoginId(), heartDto);
        return ResponseEntity.ok("찜완료");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class HeartDto {
        private Long postId;
        private Boolean isLiked;
    }
}
