package busanhackathon.team4.post.service;

import busanhackathon.team4.heart.entity.Heart;
import busanhackathon.team4.heart.repository.HeartRepository;
import busanhackathon.team4.member.entity.Member;
import busanhackathon.team4.member.repository.MemberRepository;
import busanhackathon.team4.post.dto.PostDto;
import busanhackathon.team4.post.dto.PostFormDto;
import busanhackathon.team4.post.entity.Post;
import busanhackathon.team4.post.repository.PostRepository;
import busanhackathon.team4.recipe.entity.Recipe;
import busanhackathon.team4.recipe.repository.RecipeRepository;
import busanhackathon.team4.recipe.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final HeartRepository heartRepository;
    private final RecipeService recipeService;
    private final RecipeRepository recipeRepository;

    public Long enrollPost(String username, PostFormDto postFormDto) {
        // 해당 레시피로 등록한 게시글이 있는지 체크
        Optional<Post> existPost = postRepository.findByRecipeId(postFormDto.getRecipeId());
        if (existPost.isPresent()) {
            log.info("현재 등록하고자 하는 레시피로 이미 등록된 게시글이 있습니다.");
            return null;
        }

        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new EntityNotFoundException("회원이 없습니다."));

        Recipe recipe = recipeRepository.findById(postFormDto.getRecipeId())
                .orElseThrow(() -> new EntityNotFoundException("없는 레시피입니다."));

        Post post = Post.builder()
                .title(postFormDto.getTitle())
                .content(postFormDto.getContent())
                .member(member)
                .recipe(recipe)
                .build();

        //게시글 저장
        postRepository.save(post);

        // 게시글 등록 했으면 해당 recipe public 으로 권한 변경
        recipeService.changePublic(username, post.getRecipe().getId());
        return post.getId();
    }

    @Transactional(readOnly = true)
    public List<PostDto> findAllPost(String username) {
        // 회원이 누른 heart 조회
        List<Heart> heartList = heartRepository.findByMemberId(username);
        log.info("회원이 누른 heart 조회");
        // 회원이 좋아요 누른 post
        HashMap<Long, Post> heartedPostMap = new HashMap<>();
        for (Heart heart : heartList) {
            heartedPostMap.put(heart.getPost().getId(), heart.getPost());
        }
        log.info("회원이 찜한 post hashMap 으로 변환");

        List<PostDto> postDtoList = postRepository.findAllPost()
                .stream().map(post -> {
                    boolean isHeart = heartedPostMap.containsKey(post.getId());
                    return PostDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .createdBy(post.getMember().getNickname())
                            .viewCount(post.getViewCount())
                            .isHeart(isHeart) // 현재 로그인한 회원이 찜 한 게시글인지 체크 | 했으면 true
                            .createdAt(post.getCreatedAt())
                            .heartCount(post.getHeartList().size())
                            .build();
                })
                .collect(Collectors.toList());
        log.info("postDtoList 변환 완료");
        return postDtoList;
    }

    @Transactional(readOnly = true)
    public List<PostDto> findHeartPost(String username) {
        // 회원이 누른 heart 조회
        List<Long> postIdList = heartRepository.findByMemberId(username).stream()
                .map(heart -> heart.getPost().getId())
                .collect(Collectors.toList());

        List<PostDto> postDtoList = postRepository.findByPostIdList(postIdList).stream()
                .map(p -> PostDto.builder()
                        .postId(p.getId())
                        .title(p.getTitle())
                        .content(p.getContent())
                        .viewCount(p.getViewCount())
                        .createdBy(p.getMember().getNickname())
                        .isHeart(true)
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return postDtoList;
    }

    public PostDto findOnePost(String username, Long postId) {
        List<Heart> memberHeartList = heartRepository.findByMemberId(username);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("없는 게시글입니다."));

        log.info("조회수 1 증가");
        post.increaseViewCount();

        // 회원이 찜한 게시글인지 체크
        Boolean isHeart = false;
        for(Heart heart : memberHeartList) {
            if (heart.getPost().getId() == post.getId()) {
                isHeart = true;
                break;
            }
        }

        PostDto postDto = PostDto.builder()
                .postId(post.getId())
                .title(post.getTitle()) //음식이름
                .content(post.getContent()) //전체 지피티 내용
                .viewCount(post.getViewCount())
                .createdBy(post.getMember().getNickname())
                .isHeart(isHeart)
                .createdAt(post.getCreatedAt())
                .build();
        return postDto;
    }
}
