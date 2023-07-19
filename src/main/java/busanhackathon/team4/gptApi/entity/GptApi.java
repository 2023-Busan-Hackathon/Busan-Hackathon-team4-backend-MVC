package busanhackathon.team4.gptApi.entity;

import busanhackathon.team4.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GptApi extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "gpt_api_id")
    private Long id;
    @Column(columnDefinition = "text")
    private String gptResponse;
    private String food;
    @Column(columnDefinition = "text")
    private String ingredient;
    @Column(columnDefinition = "text")
    private String recipe;
}
