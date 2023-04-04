package ezenweb.example.day02.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// 스프링 관리하는 IOC 컨테이너 빈[객체] 등록
@RestController// @ResponseBody + @Controller
@Slf4j      // 스프링 로그 메소드 제공[ 레벨 :trace < debug < info < warn < error ] ]
public class MappingController3 {
    @GetMapping("/blue")
    public String getBlue() {

        log.info("클라이언트로부터 getBlue 메소드 요청이 들어옴");
        return "정상 응답";
    }

    @PostMapping("/blue")
    public String postBlue() {
        log.info("클라이언트로부터 postBlue메소드 요청이 들어옴");
        return "정상 응답";
    }

    @PutMapping("/blue")
    public String putBlue() {
        log.info("클라이언트로부터 putBlue메소드 요청이 들어옴");
        return "정상 응답";
    }

    @DeleteMapping("/blue")
    public String deleteBlue() {
        log.info("클라이언트로부터 deleteBlue메소드 요청이 들어옴");
        return "정상 응답";
    }
}
/*
            스프링 부트 동작 구조


    크롬/ajax/js --------요청-------->  서블릿 컨테이너    -------> Dispatcher Servlet
            http://localhost:8080/orange                            핸들러 매핑으로 해당 컨트롤러( 스프링 컨테이너 ) 검색
                                                        -------> Mapping 검색
                                                                    @RequestMapping(value = "/orange" , method = RequestMethod.GET )
                  <----------------------- 응답 -----------------------
 */