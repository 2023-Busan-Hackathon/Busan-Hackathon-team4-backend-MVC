package busanhackathon.team4.gptApi;

import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GptApiController {

    private final GptApiService service;

    @PostMapping("/ai")
    @ResponseBody
    public ResponseEntity<GptApiDto> openAiGPT(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               @RequestBody GptApiFormDto gptApiFormDto,
                                               Model model) {
        log.info("ai 컨트롤러 진입");
        try {
            GptApiDto gptApiDto = service.callApi(principalDetails.getLoginId(), gptApiFormDto);
            return ResponseEntity.ok(gptApiDto);
        } catch (HttpClientErrorException e) {
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/ai")
    public String aiForm() {
        return "gpt/gptForm";
    }

    @PostMapping("/ai/history")
    public String getHistory() {
        GptApiDto gptApiDto = service.getOneHistory();
        return null;
    }
}