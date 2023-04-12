package ezenweb.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration // 스프링 빈에 등록 [ MVC 컴포넌트 ]
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    // configure(HttpSecurity http) http[URL] 관련 보안 담당 메소드
    @Override // 재정의 [ 코드 바꾸기 ]
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);  // super : 부모클래스 호출
        http
                .csrf() // 사이트 간 요청 위조 [ post , put 사용 불가능 ]
                    .ignoringAntMatchers("/member/info")    // 특정 매핑 URL csrf 무시
                    .ignoringAntMatchers("/member/login")
                .and()// 기능 추가 / 구분할때 사용되는 메소드
                    .formLogin()
                        .loginPage("/member/login") // 로그인으로 사용될 페이지 의 URL
                        .loginProcessingUrl("/member/login") // 로그인을 처리할 매핑 URL
                        .defaultSuccessUrl("/") // 로그인 성공했을때 이동할 매핑 URL
                        .failureUrl("/member/login") // 로그인 실패했을때 이동할 매핑 URL
                        .usernameParameter("memail") // 로그인시 사용될 계정 아이디 의 필드명
                        .passwordParameter("mpassword") // 로그인시 사용될 계정 패스워드 의 필드명
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))// 로그아웃 처리 를 요청할 매핑 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공했을때 이동할 매핑 URL
                        .invalidateHttpSession(true); // 세션 초기화 x
    }
}
