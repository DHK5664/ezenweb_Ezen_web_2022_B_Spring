package ezenweb.web.service;

import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.member.MemberEntity;
import ezenweb.web.domain.member.MemberEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

// UserDetailsService : 일반유저 서비스 ---> loadUserByUsername 구현
// OAuth2UserService : oauth2 유저 서비스 구현 ---> loadUser 구현

@Service // 서비스 레이어
@Slf4j
public class MemberService implements UserDetailsService , OAuth2UserService<OAuth2UserRequest , OAuth2User> {
    @Override // 토큰 결과 [JSON{필드명:값 , 필드명:값} VS Map{키=값 , 키=값 , 키=값}]
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 인증[로그인] 결과 토큰 확인
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
            log.info("서비스 정보 : " +oAuth2UserService.loadUser(userRequest));
        // 2. 전달받은 토큰을 이용한 회원정보 요청
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
            log.info("회원정보 : " +oAuth2User.getAuthorities());

        // !!!! oAuth2User.getAttributes() map<String , Object>구조
        // {sub=115042686849095214337, name=김동혁, given_name=동혁,email=sunsinger5664@gmail.com}
            // 구글의 이메일 호출
                String email =  (String)oAuth2User.getAttributes().get("email");
                    log.info("google email :" +email);
            // 구글의 이름 호출
                String name = (String)oAuth2User.getAttributes().get("name");
                    log.info("google name :" +name);

        // 인가 객체 [OAuth2User ---> 통합Dto(일반+oauth)]
        MemberDto memberDto = new MemberDto();
        memberDto.set소셜회원정보(oAuth2User.getAttributes());
        memberDto.setMemail(email);
        memberDto.setMname(name);
            Set<GrantedAuthority> 권한목록 = new HashSet<>();
            SimpleGrantedAuthority 권한 = new SimpleGrantedAuthority("ROLE_oauthuser");
            권한목록.add(권한);
        memberDto.set권한목록(권한목록);

        // 1. DB 저장하기 전에 해당 이메일로 된 이메일 존재하는지 검사
        MemberEntity entity = memberEntityRepository.findByMemail(email);
        if(entity == null){ // 첫방문
            // DB처리 [ 첫 방문시에만 db등록 , 두번째 방문시 부터는 db수정  ]
            memberDto.setMrole("oauthuser");
            memberEntityRepository.save(memberDto.toEntity());
        }else{ // 두번째 방문이상 수정처리
            entity.setMname(name);
        }
        return memberDto;
    }

    @Autowired
    private MemberEntityRepository memberEntityRepository;
    @Autowired private HttpServletRequest request; // 요청 객체

    // 1. 일반 회원가입 [ 본 어플리케이션에서 가입한 사람 ]
    @Transactional
    public boolean write(MemberDto memberDto){
        // 스프링 시큐리티에서 제공하는 암호화[ 사람이 이해하기 어렵고 컴퓨터는 이해할 수 있는 단어 ] 사용하기
            // DB 내에서도 패스워드 감추기 위해서 , 정보가 이동하면서 패스워드가 노출 될 수 있어서 방지용
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                // 인코더 : 특정 형식으로 변경 // 디코더 : 원본으로 되돌리기
        log.info("비크립트 암호화 사용 : " +passwordEncoder.encode("1234"));
        // 입력받은[DTO] 패스워드 암호화 해서 다시 DTO에 저장하자.
        memberDto.setMpassword(passwordEncoder.encode(memberDto.getMpassword()));

        // 등급 부여
        memberDto.setMrole("user");

        MemberEntity entity= memberEntityRepository.save(memberDto.toEntity());
        if(entity.getMno()>0){return true;}
        return false;
    }
    // **** 로그인 [ 시큐리티 사용 했을때 ]
        // 시큐리티 API [ 누군가 미리 만들어진 메소드 안에서 커스터마이징[ 수정 ] ]

    // *2. 로그인 [시큐리티 사용하기 전]
    /*
    @Transactional
    public boolean login(MemberDto memberDto) {

        // 1. 입력받은 이메일로 엔티티 찾기
        MemberEntity entity = memberEntityRepository.findByMemail(memberDto.getMemail()); log.info("entity = " + entity);
            
        // 2. 찾은 엔티티 안에는 암호화된 패스워드
            // 엔티티안에 있는 패스워드와 입력받은 패스워드[암호화된 상태]와 입력받은 패스워드[안된 상태]와 비교
        if( new BCryptPasswordEncoder().matches( memberDto.getMpassword() , entity.getMpassword()) ) {
                // 세션사용 : 메소드 밖 필드에 @Autowired private HttpServletRequest request;
            request.getSession().setAttribute("login" , entity.getMemail());
            return true;
        }
        */
        /*
        // 2. 입력받은 이메일과 패스워드가 동일한지
        Optional<MemberEntity> result = memberEntityRepository
                .findByMemailAndMpassword(memberDto.getMemail() , memberDto.getMpassword());
        log.info("result = " + result.get());
        if(result.isPresent()){
            request.getSession().setAttribute("login" , result.get().getMno());
            return true;
        }
        return false;*/
        /* 3.

            boolean result = memberEntityRepository.existsByMemailAndMpassword(memberDto.getMemail(), memberDto.getMpassword());
                log.info("result = " + result);
            if (result == true) { request.getSession().setAttribute("login", memberDto.getMemail()); return true; }

            return false;
        }
         */


    // 3. 회원수정
    @Transactional
    public boolean update( MemberDto memberDto){
        Optional<MemberEntity> entityOptional
                = memberEntityRepository.findById(memberDto.getMno());
        if(entityOptional.isPresent()){
            MemberEntity entity = entityOptional.get();
                entity.setMname(memberDto.getMname());entity.setMphone(memberDto.getMphone());
                entity.setMrole(memberDto.getMrole());entity.setMpassword(memberDto.getMpassword());
        }
        return false;
    }
    // 4. 회원탈퇴
    @Transactional
    public boolean delete( int mno){
        Optional<MemberEntity> entityOptional = memberEntityRepository.findById(mno);
        if(entityOptional.isPresent()){
            memberEntityRepository.delete(entityOptional.get()); return true;
        }
        return false;
    }
    // [ 스프링 시큐리티 적용했을때 사용되는 로그인 메소드 ]
    @Override
    public UserDetails loadUserByUsername(String memail) throws UsernameNotFoundException {
        // 1. implements UserDetailsService 인터페이스 구현
        // 2. loadUserByUsername() 메소드 : 아이디 검증
            // 패스워드 검증 [ 시큐리티 자동 ]
        MemberEntity entity = memberEntityRepository.findByMemail(memail);
        if(entity == null){return null;}

        // 3. 검증 후 세션에 저장할 DTO 반환
        MemberDto dto = entity.toDto();
            // Dto 권한(여러개) 넣어주기
        // 1. 권한목록 만들기
        Set<GrantedAuthority> 권한목록 = new HashSet<>();
        // 2. 권한객체 만들기 [DB에 존재하는 권한명(ROLE_!!!!)으로 ]
            // 권한 없을경우 : ROLE_ANONYMOUS / 권한 있을경우 ROLE_권한명 : ROLE_admin , ROLE_user
        SimpleGrantedAuthority 권한명 = new SimpleGrantedAuthority("ROLE_"+entity.getMrole() );
        // 3. 만든 권한 객체를 권한목록[컬렉션]에 추가
        권한목록.add(권한명);
        // 4. UserDetails 에 권한목록 대입
        dto.set권한목록(권한목록);

        log.info("dto = " + dto);
        return dto;  // UserDetails : 원본 데이터의 검증할 계정 , 패스워드 포함  // MemberDto가 UserDetails를 implements해서 자식은 부모가 될 수 있다는 다형성에 의해 리턴 가능
    }

    // 2. [세션에 존재하는]회원정보
    @Transactional
    public MemberDto info(){
        // 1. 시큐리티 인증[로그인] UserDetails객체[세션]
            // SecurityContextHolder : 시큐리티 정보 저장소
            // SecurityContextHolder.getContext() : 시큐리티 저장된 정보호출
            // SecurityContextHolder.getContext().getAuthentication();  인증 전체 정보 호출
            log.info("Auth : " + SecurityContextHolder.getContext().getAuthentication());
            log.info("Auth : " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 인증된 회원의 정보 호출
        if(o.equals("anonymousUser")){return null;}
            // [ Principal ]
            // 인증 실패시 : anonymousUser
            // 인증 성공시 : 회원정보[Dto]

        // 2. 인증된 객체 내 회원정보[ Principal ] 타입 변환
        return (MemberDto) o; // Object ---> dto

        /*
        // 2. 일반 세션으로 로그인 정보를 관리했을떄 [jsp]

        String memail = (String) request.getSession().getAttribute("login");
        if(memail != null){
            MemberEntity entity = memberEntityRepository.findByMemail(memail);
            return entity.toDto();
        }

        return null;

         */
    }
    /*
    // 2. [ 세션에 존재하는 정보 제거 ] 로그아웃
    @Transactional
    public boolean logout(){ request.getSession().setAttribute("login" , null); return true;}*/

    // 과제 : 이메일 찾기
    public String findid(String mname , String mphone){
        Optional<MemberEntity> entity = memberEntityRepository.findByMnameAndMphone(mname,mphone);

        if( entity.isPresent() ){
            return entity.get().getMemail();
        }else{
            return null;
        }


    }

    public String findpw(String memail , String mphone){
        Optional<MemberEntity> entity = memberEntityRepository.findByMemailAndMpassword(memail,mphone);

        if(entity.isPresent()){
            return entity.get().getMpassword();
        }else {
            return null;
        }
    }

}

// == 스택 메모리 내 데이터 비교
// .equals 힙 메모리 내 데이터 비교
// .matches() : 문자열 주어진 패턴 포함 동일여부 체크