package busanhackathon.team4.post.controller;

import busanhackathon.team4.post.dto.PostDto;
import busanhackathon.team4.post.dto.PostFormDto;
import busanhackathon.team4.post.service.PostService;
import busanhackathon.team4.recipe.dto.RecipeDto;
import busanhackathon.team4.recipe.service.RecipeService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    private final RecipeService recipeService;

    /**
     * 게시글 목록
     */
    @GetMapping("/postList")
    public String getPostList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<PostDto> postDtoList = postService.findAllPost(principalDetails.getLoginId());
        model.addAttribute("postDtoList", postDtoList);
        return "/post/postList";
    }

    /**
     * 게시글 상세
     */
    @GetMapping("/post/{postId}")
    public String getPostDetail(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                @PathVariable("postId") Long postId,
                                Model model) {
        PostDto postDto = postService.findOnePost(principalDetails.getLoginId(), postId);
        model.addAttribute("postDto", postDto);
        return "/post/postDetail";
    }

    /**
     * 게시글 등록 폼 이동
     */
    @GetMapping("/postForm/{recipeId}")
    public String postForm(@PathVariable("recipeId") Long recipeId, Model model) {
        RecipeDto recipeDto = recipeService.findOneById(recipeId, false);
        model.addAttribute("recipeDto", recipeDto);
        return "/post/postForm";
    }

    /**
     * 게시글 등록
     */
    @PostMapping("/post")
    public String enrollPost(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             PostFormDto postFormDto) {
        Long postId = postService.enrollPost(principalDetails.getLoginId(), postFormDto);
        return "redirect:/postList";
    }

    /**
     * 찜목록
     */
    @GetMapping("/post-heart")
    public String getHeartPostList(@AuthenticationPrincipal User user, Model model) {
        List<PostDto> postDtoList = postService.findHeartPost(user.getUsername());
        model.addAttribute("postDtoList", postDtoList);
        return "/post/postList";
    }


}
