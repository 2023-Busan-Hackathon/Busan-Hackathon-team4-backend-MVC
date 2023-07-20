package busanhackathon.team4.heart.repository;

import busanhackathon.team4.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {

    @Query("select h from Heart h join fetch h.post p where h.member.loginId = :username")
    List<Heart> findByMemberId(@Param("username") String username);

    @Query("select count(h) from Heart h where h.member.loginId = :username")
    Integer findHeartCountByLoginId(@Param("username") String username);

    @Query("select h from Heart h where h.member.id = :memberId and h.post.id = :postId")
    Optional<Heart> findByMemberIdAndPostId(@Param("memberId") Long memberId, @Param("postId") Long postId);
}
