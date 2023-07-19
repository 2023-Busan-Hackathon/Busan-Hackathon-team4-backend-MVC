package busanhackathon.team4.recipe.controller;

import busanhackathon.team4.recipe.dto.RecipeDto;
import busanhackathon.team4.recipe.service.RecipeService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class RecipeController {
    private final RecipeService recipeService;

    /**
     * 레시피 목록
     */
    @GetMapping("/recipe")
    public String getRecipeList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<RecipeDto> recipeDtoList = recipeService.findAllRecipeByMember(principalDetails.getLoginId());
        model.addAttribute("recipeDtoList", recipeDtoList);

        return "recipe/recipeList";
    }

    /**
     * 레시피 상세
     */
    @GetMapping("/recipe/{recipeId}")
    public String getRecipeDetail(@PathVariable("recipeId") Long recipeId, Model model) {
        RecipeDto recipeDto = recipeService.findOneById(recipeId);
        model.addAttribute("recipeDto", recipeDto);
        return "recipe/recipeDetail";
    }

    /**
     * 지피티에서 저장할래요 누르면 저장 하는 url
     */
    @PostMapping("/recipe")
    public String saveRecipe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             RecipeDto recipeDto) {
        Long recipeId = recipeService.saveRecipe(principalDetails.getLoginId(), recipeDto);
        return "redirect:/recipe";
    }

    /**
     * 레시피 제거
     */
    @DeleteMapping("/recipe/{recipeId}")
    public String removeRecipe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable("recipeId") Long recipeId) {
        recipeService.removeRecipe(principalDetails.getLoginId(), recipeId);
        return "redirect:/recipe";
    }

}
