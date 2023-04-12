package ezenweb.web.controller;

import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController // @Controller + @ResponseBody
@Slf4j  // 로그기능주입
@RequestMapping("/member")
public class MemberController {
    @GetMapping("/signup") // localhost:8080/member/signup 요청시 아래 템플릿에 반환
    public Resource getSignup(){return new ClassPathResource("templates/member/signup.html");}
    @GetMapping("/login")
    public Resource getLogin(){return new ClassPathResource(("templates/member/login.html"));}
    // 1.@Autowired 없을때 객체[빈] 생성
        // MemberService service = new MemberService();
    // 2.@Autowired 있을때 객체[빈] 자동생성
    @Autowired
    MemberService memberService;

    // 1. [C]회원가입
    @PostMapping("/info")   // URL 같아도 HTTP 메소드 다르므로 식별 가능
    public boolean write( @RequestBody MemberDto memberDto){ // 단 JAVA 클래스내 메소드 이름은 중복 불가능
        log.info(" member info write : " + memberDto);
        boolean result = memberService.write(memberDto);
        return result;
    }
    // 2. 회원정보[세션] 로그아웃
    @GetMapping("/logout")public boolean logout(){return memberService.logout();}
    // 2. [R]회원정보 호출
    @GetMapping("/info")
    public MemberDto info( ){ MemberDto result = memberService.info(); return result; }
    // 3. [U]회원정보 수정
    @PutMapping("/info")
    public boolean update( @RequestBody MemberDto memberDto){
        log.info(" member info update : " + memberDto);
        boolean result = memberService.update((memberDto));
        return false;
    }

    // 4. [D]회원정보 탈퇴
    @DeleteMapping("/info")
    public boolean delete( @RequestParam int mno){
        log.info(" member info delete : " + mno);
        boolean result = memberService.delete(mno);
        return false;
    }
    // --------------------- 스프링 시큐리티 사용하기 전 ---------------------- //
    @PostMapping("/login")
    public boolean login(@RequestBody MemberDto memberDto){
        boolean result = memberService.login(memberDto);
        return result;
    }
}
