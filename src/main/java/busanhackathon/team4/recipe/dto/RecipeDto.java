package busanhackathon.team4.recipe.dto;


import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecipeDto {
    private Long recipeId;
    private String foodName;
    private String method;
    private String gptResponse;
    private String ingredient;
    private Boolean isPublic;

    private LocalDateTime createdAt;
}
