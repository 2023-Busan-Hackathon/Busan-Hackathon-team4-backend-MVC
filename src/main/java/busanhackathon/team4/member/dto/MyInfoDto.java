package busanhackathon.team4.member.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MyInfoDto {
    private Long memberId;
    private String nickname;
    private Integer heartCount;
}
