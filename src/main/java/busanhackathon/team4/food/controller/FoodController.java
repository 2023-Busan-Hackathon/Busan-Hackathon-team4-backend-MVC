package busanhackathon.team4.food.controller;

import busanhackathon.team4.food.dto.FoodDto;
import busanhackathon.team4.food.service.FoodService;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/food")
    public String getFoodList(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        List<FoodDto> foodDtoList = foodService.findAllFoodByMember(principalDetails.getLoginId());
        model.addAttribute("foodDtoList", foodDtoList);
        return "/food/foodList";
    }

    @PostMapping("/food-enroll")
    public String banFood(@AuthenticationPrincipal PrincipalDetails principalDetails, FoodDto foodDto) {
        Long foodId = foodService.enrollFood(principalDetails.getLoginId(), foodDto);
        return "redirect:/food";
    }

    @PostMapping("/food/{foodId}")
    public String removeBanFood(@PathVariable("foodId") Long foodId) {
        foodService.removeFood(foodId);
        return "redirect:/food";
    }
}
