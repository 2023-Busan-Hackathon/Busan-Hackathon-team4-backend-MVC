package busanhackathon.team4.member.service;

import busanhackathon.team4.member.controller.MemberController;
import busanhackathon.team4.member.entity.Member;
import busanhackathon.team4.member.entity.Role;
import busanhackathon.team4.member.repository.MemberRepository;
import busanhackathon.team4.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    private final PasswordEncoder encoder;
    private final BCryptPasswordEncoder encoder;

    /**
     * 회원가입
     */
    public Long saveMember(MemberController.JoinFormDto joinFormDto) {
        
        //중복체크
        Optional<Member> existMember = memberRepository.findByLoginId(joinFormDto.getLoginId());
        if(!existMember.isEmpty()) throw new IllegalStateException("이미 존재하는 회원입니다.");

        Member member = Member.builder()
                .loginId(joinFormDto.getLoginId())
                .password(encoder.encode(joinFormDto.getPassword()))
                .nickname(joinFormDto.getNickname())
                .role(Role.ROLE_MEMBER) // 기본 권한 설정
                .build();
        return memberRepository.save(member).getId();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 메소드 실행");
        Optional<Member> findMember = memberRepository.findByLoginId(username);
        if (!findMember.isPresent()) {
            throw new UsernameNotFoundException("가입되지 않은 회원입니다."); //스프링 내부에서 BadCredentialsException 예외로 변형시켜버림
        }
        Member member = findMember.get();

        log.info("loadUserByUsername 메소드 종료");
        return new PrincipalDetails(member.getId() ,member.getLoginId(), member.getPassword(), member.getNickname(), member.getRole()); //세션에 커스텀한 PrincipalDetails 저장

    }

}
