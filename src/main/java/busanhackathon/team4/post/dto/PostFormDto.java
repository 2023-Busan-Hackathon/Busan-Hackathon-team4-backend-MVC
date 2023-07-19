package busanhackathon.team4.post.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostFormDto {
    private String title;
    private Long recipeId;
    private String content;
}
