package ezenweb.web.config;

import ezenweb.web.controller.AuthSuccessFailHandler;
import ezenweb.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration // 스프링 빈에 등록 [ MVC 컴포넌트 ]
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthSuccessFailHandler authSuccessFailHandler;

    // 인증[로그인] 관련 보안 담당 메소드
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(new BCryptPasswordEncoder() );
        // auth.userDetailsService()    : userDetailsService가 구현된 서비스 대입
        // .passwordEncoder( new BCryptPasswordEncoder() )서비스 안에서 로그인 시 패스워드 검증시 사용된 암호화 객체 대입
    }

    // configure(HttpSecurity http) http[URL] 관련 보안 담당 메소드
    @Override // 재정의 [ 코드 바꾸기 ]
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);  // super : 부모클래스 호출
        http// 기능 추가 / 구분할때 사용되는 메소드
                    .formLogin()
                        .loginPage("/member/login") // 로그인으로 사용될 페이지 의 URL
                        .loginProcessingUrl("/member/login") // 로그인을 처리할 매핑 URL -> html에서 submit시 여기로 도착
                        //.defaultSuccessUrl("/") // 로그인 성공했을때 이동할 매핑 URL
                        .successHandler(authSuccessFailHandler)
                        //.failureUrl("/member/login") // 로그인 실패했을때 이동할 매핑 URL
                        .failureHandler(authSuccessFailHandler)
                        .usernameParameter("memail") // 로그인시 사용될 계정 아이디 의 필드명
                        .passwordParameter("mpassword") // 로그인시 사용될 계정 패스워드 의 필드명
                .and()
                    .logout()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))// 로그아웃 처리 를 요청할 매핑 URL
                        .logoutSuccessUrl("/") // 로그아웃 성공했을때 이동할 매핑 URL
                        .invalidateHttpSession(true) // 세션 초기화 x
                .and()
                    .oauth2Login() // 소셜 로그인 설정
                    .defaultSuccessUrl("/") // 로그인 성공시 이동할 매핑 URL
                    //.successHandler(authSuccessFailHandler)
                    .userInfoEndpoint()
                    .userService(memberService); // oauth2 서비스를 처리 할 서비스 구현

        http.cors(); // CORS 정책 사용
        http.csrf().disable(); // csrf 사용 해제

    }// configure end

    // import org.springframework.web.cors.CorsConfigurationSource;
    // 스프링 시큐리티에 CORS 정책 설정 [ 리액트[3000]의 요청 받기 위해서 ]
  /*  @Bean // 빈 등록
    CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));    // 주소
        corsConfiguration.setAllowedMethods(Arrays.asList("HEAD","GET","POST","PUT","DELETE")); //http메소드
        corsConfiguration.setAllowedHeaders(Arrays.asList("Content-Type" , "Cache-Control" , "Authorization")); // http설정
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",corsConfiguration);
        return source;
    }
*/

}// SecurityConfiguration class end

/*
    http 오류
        404 : 페이지 없거나 , 경로 문제
        403 : 권한 문제

*/







