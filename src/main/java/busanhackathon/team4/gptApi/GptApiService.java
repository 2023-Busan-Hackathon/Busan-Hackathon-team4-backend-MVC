package busanhackathon.team4.gptApi;

import busanhackathon.team4.food.dto.FoodDto;
import busanhackathon.team4.food.service.FoodService;
import busanhackathon.team4.gptApi.entity.GptApi;
import busanhackathon.team4.gptApi.repository.GptApiRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GptApiService {

    private final FoodService foodService;
    private final GptApiRepository gptApiRepository;

    public GptApiDto callApi(String username, GptApiFormDto gptApiFormDto) {
        try {
            List<FoodDto> foodDtoList = foodService.findAllFoodByMember(username);
            String exceptFood = foodDtoList.stream()
                    .map(FoodDto::getFoodName)
                    .collect(Collectors.joining(", "));

            String query = gptApiFormDto.getIngredient() + "\n" + exceptFood + "\n" + gptApiFormDto.getDifficulty();

            System.out.println("=====쿼리=====");
            System.out.println(query);
            System.out.println("=====쿼리=====");

            // 파이썬 실행 명령과 파일 경로
//            String command = "python";
//            String filePath = "src/main/java/busanhackathon/team4/gptApi/gptApi.py";
//
//
//            ProcessBuilder processBuilder = new ProcessBuilder(command, filePath, query);
//            Process process = processBuilder.start();
//
//            // 실행 결과 읽기
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//
//            // 오류 읽기
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));


            String result = "### 음식이름:\n" +
                    "- 김치 우유 볶음\n" +
                    "\n" +
                    "### 재료:\n" +
                    "- 당근\n" +
                    "- 우유\n" +
                    "- 밀가루\n" +
                    "- 설탕\n" +
                    "- 계란\n" +
                    "- 베이킹 파우더\n" +
                    "- 소금\n" +
                    "- 바닐라 추출물\n" +
                    "- 식용유\n" +
                    "- 크림치즈\n" +
                    "- 파우더 슈가\n" +
                    "\n" +
                    "### 레시피:\n" +
                    "1. 당근을 손질하여 껍질을 벗기고 가늘게 다지세요.\n" +
                    "2. 당근 다진 것과 우유를 함께 넣은 볼에 좋은 섞여질 때까지 섞어주세요.\n" +
                    "3. 다른 볼에 밀가루, 설탕, 계란, 베이킹 파우더, 소금, 바닐라 추출물을 넣고 섞어주세요.\n" +
                    "4. 3번 볼에 2번 볼의 당근과 우유를 넣고 섞어주세요.\n" +
                    "5. 팬에 식용유를 두르고 4번 볼의 혼합물을 부어서 약한 불에서 국자로 반죽의 가득 차지 않도록 튀겨주세요.\n" +
                    "6. 팬이 뜨거워질 때까지 기다리고 천천히 뒤집어서 튀깁니다.\n" +
                    "7. 반복해서 5번과 6번을 하여 반죽을 전부 사용합니다.\n" +
                    "8. 크림치즈와 파우더 슈가를 섞어서 당근 케이크 위에 발라주시면 완성입니다!";
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);  // 실행 결과 출력
//                result += line + "\n";
//            }
//            while ((line = errorReader.readLine()) != null) {
//                log.info("예외터짐");
//                System.out.println(line);
//                result += line + "\n";
//            }


            // Extracting the recommended food name
            String food = "";
            String[] lines = result.split("\n");


            boolean startCollectingRecipe0 = false;
            for (String l : lines) {
                if (startCollectingRecipe0) {
                    food = l;
                    break;
                }
                if (l.contains("### 음식")) {
                    log.info("음식 추출");
                    startCollectingRecipe0 = true;
                }
            }

            // Extracting the 재료
            StringBuilder ingredientBuilder = new StringBuilder();
            boolean startCollectingRecipe1 = false;
            for (String l : lines) {
                if (startCollectingRecipe1) {
                    if (l.trim().isEmpty()) {
                        break;
                    }
                    ingredientBuilder.append(l).append("\n");
                }
                if (l.contains("### 재료")) {
                    log.info("재료 추출");
                    startCollectingRecipe1 = true;
                }
            }
            String ingredient = ingredientBuilder.toString().trim();

            // Extracting the recipe
            StringBuilder recipeBuilder = new StringBuilder();
            boolean startCollectingRecipe2 = false;
            for (String l : lines) {
                if (startCollectingRecipe2) {
                    if (l.trim().isEmpty()) {
                        break;
                    }
                    recipeBuilder.append(l).append("\n");
                }
                if (l.contains("### 레시피")) {
                    log.info("레시피 추출");
                    startCollectingRecipe2 = true;
                }
            }
            String recipe = recipeBuilder.toString().trim();

            // Printing the extracted information
            System.out.println("요리할 음식: " + food);
            System.out.println("필요한 재료: " + ingredient);
            System.out.println("레시피: " + recipe);

            GptApi gptApiEntity = GptApi.builder()
                    .gptResponse(result)
                    .food(food)
                    .ingredient(ingredient)
                    .recipe(recipe)
                    .build();
            //gpt 응답 데이터 저장
            gptApiRepository.save(gptApiEntity);


            GptApiDto responseDto = GptApiDto.builder()
                    .gptResponse(result)
                    .food(food)
                    .ingredient(ingredient)
                    .recipe(recipe)
                    .build();

            // 프로세스 종료 대기
//            int exitCode = process.waitFor();
//            System.out.println("Exit Code: " + exitCode);
            return responseDto;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public GptApiDto getOneHistory() {
        GptApi gptApi = gptApiRepository.findOneDesc()
                .orElseThrow(() -> new EntityNotFoundException("저장된 gpt 응답 데이터가 없습니다."));
        GptApiDto gptApiDto = GptApiDto.builder()
                .gptResponse(gptApi.getGptResponse())
                .food(gptApi.getFood())
                .ingredient(gptApi.getIngredient())
                .recipe(gptApi.getRecipe())
                .build();
        return gptApiDto;
    }
}