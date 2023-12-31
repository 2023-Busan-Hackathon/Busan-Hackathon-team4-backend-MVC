package busanhackathon.team4.recipe.service;

import busanhackathon.team4.member.entity.Member;
import busanhackathon.team4.member.repository.MemberRepository;
import busanhackathon.team4.recipe.dto.RecipeDto;
import busanhackathon.team4.recipe.entity.Recipe;
import busanhackathon.team4.recipe.repository.RecipeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final MemberRepository memberRepository;

    /**
     * api 에서 받아온 레시피 저장
     */
    public Long saveRecipe(String username, RecipeDto recipeDto) {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        Recipe recipe = Recipe.builder()
                .foodName(recipeDto.getFoodName())
                .method(recipeDto.getMethod())
                .ingredient(recipeDto.getIngredient())
                .gptResponse(recipeDto.getGptResponse())
                .isPublic(false)
                .member(member)
                .build();
        recipeRepository.save(recipe);
        log.info("레시피 저장 완료");
        return recipe.getId();
    }

    /**
     * 레시피 삭제
     * 관련된 게시글이 있을 경우 게시글과 좋아요 함께 삭제
     */
    public void removeRecipe(String username, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("없는 레시피입니다."));

        if (!recipe.getMember().getLoginId().equals(username)) {
            log.info("본인이 저장한 레시피만 삭제할 수 있습니다.");
        }
        recipeRepository.deleteById(recipeId);
        log.info("레시피 삭제 완료");
    }

    /**
     * 레시피 전체 조회
     */
    @Transactional(readOnly = true)
    public List<RecipeDto> findAllRecipeByMember(String username) {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        List<RecipeDto> recipeDtoList = recipeRepository.findAllRecipeByMember(member.getId()).stream()
                .map(recipe -> RecipeDto.builder()
                        .recipeId(recipe.getId())
                        .foodName(recipe.getFoodName())
                        .method(recipe.getMethod())
                        .gptResponse(recipe.getGptResponse())
                        .ingredient(recipe.getIngredient())
                        .isPublic(recipe.getIsPublic())
                        .build())
                .collect(Collectors.toList());
        return recipeDtoList;
    }

    /**
     * 공개 & 비공개 설정
     */
    public void changePublic(String username, Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("없는 레시피입니다."));
        if (!recipe.getMember().getLoginId().equals(username)) {
            log.info("본인이 작성할 글만 권한 공개 설정 가능합니다.");
        }
        // true 값으로 update
        recipe.setIsPublic(true);
    }

    /**
     * 레시피 상세 보기
     */
    @Transactional(readOnly = true)
    public RecipeDto findOneById(Long recipeId, Boolean format) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new EntityNotFoundException("없는 레시피입니다."));
        String ingredient = recipe.getIngredient();
        String method = recipe.getMethod();
        if (format) {
            ingredient = ingredient.replaceAll("\n", "<br>");
            method = method.replaceAll("\n", "<br>");
        }

        RecipeDto recipeDto = RecipeDto.builder()
                .recipeId(recipe.getId())
                .foodName(recipe.getFoodName())
                .ingredient(ingredient)
                .method(method)
                .gptResponse(recipe.getGptResponse())
                .isPublic(recipe.getIsPublic())
                .createdAt(recipe.getCreatedAt())
                .build();
        return recipeDto;
    }
}
