package busanhackathon.team4.gptApi;

import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GptApiController {

    private final GptApiService service;

    @PostMapping("/ai")
    public String openAiGPT(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            GptApiFormDto gptApiFormDto,
                            Model model) {

        log.info("ai 컨트롤러 진입");
        try {
            GptApiDto gptApiDto = service.callApi(principalDetails.getLoginId(), gptApiFormDto);
            model.addAttribute("gptApiDto", gptApiDto);
        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "다시 시도해주세요");
        }
        return "/gpt/gptForm";
    }

    @GetMapping("/ai")
    public String aiForm() {
        return "/gpt/gptForm";
    }

    @PostMapping("/ai/history")
    public String getHistory() {
        GptApiDto gptApiDto = service.getOneHistory();
        return null;
    }
}