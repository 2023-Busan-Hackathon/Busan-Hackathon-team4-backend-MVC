package busanhackathon.team4.heart.service;

import busanhackathon.team4.heart.controller.HeartController;
import busanhackathon.team4.heart.entity.Heart;
import busanhackathon.team4.heart.repository.HeartRepository;
import busanhackathon.team4.member.entity.Member;
import busanhackathon.team4.member.repository.MemberRepository;
import busanhackathon.team4.post.entity.Post;
import busanhackathon.team4.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public void enrollHeart(String username, HeartController.HeartDto heartDto) {
        Member member = memberRepository.findByLoginId(username)
                .orElseThrow(() -> new EntityNotFoundException("없는 회원입니다."));
        Post post = postRepository.findById(heartDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException("없는 게시글입니다"));
        if(heartDto.getIsLiked()) {
            Heart heart = Heart.builder()
                    .member(member)
                    .post(post)
                    .build();
            heartRepository.save(heart).getId();
            log.info("좋아요 저장");
        }
        else {
            Heart heart = heartRepository.findByMemberIdAndPostId(member.getId(), post.getId())
                    .orElseThrow(() -> new EntityNotFoundException("회원이 누른 좋아요 엔티티가 없습니다."));
            heartRepository.deleteById(heart.getId());
            log.info("좋아요 삭제");
        }
        
    }

    @Transactional(readOnly = true)
    public Integer getHeartCountByMember(String username) {
        Integer heartCount = heartRepository.findHeartCountByLoginId(username);
        return heartCount;
    }
}
