package ezenweb.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ezenweb.web.domain.member.MemberDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인증 성공했을때와 실패했을때 시큐리티의 컨트롤 재구현
// AuthenticationSuccessHandler : 인증 성공했을때에 대한 인터페이스
    // onAuthenticationSuccess : 인증이 성공했을때 실행되는 메소드
// AuthenticationFailureHandler : 인증 실패했을때에 대한 인터페이스
    // onAuthenticationFailure : 인증이 실패했을때 실행되는 메소드
@Component // 빈등록
@Slf4j // 로그
public class AuthSuccessFailHandler implements AuthenticationSuccessHandler , AuthenticationFailureHandler {

    // ObjectMapper
    // @Autowired // 빈 등록이 안되어있어서 사용불가(들어가보면 component가 없어서 안댐)
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override // 인수 : request, response, authentication : 인증 성공한 정보
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("authentication : " + authentication);
        MemberDto dto = (MemberDto)authentication.getPrincipal();
        String json = objectMapper.writeValueAsString(dto);

        // ajax에게 전송
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // ? @ResponseBody 사용 안했을때는 직접 작용
        response.getWriter().println(json);
    }

    @Override // 인수 : requset, response , exception : 예외 [인증 실패한 예외 객체]
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("exception : " + exception.toString());

        String json = objectMapper.writeValueAsString(false);

        // ajax에게 전송
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // ? @ResponseBody 사용 안했을때는 직접 작용
        response.getWriter().println(json);
    }
}
