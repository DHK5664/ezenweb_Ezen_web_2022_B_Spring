package ezenweb.example.day03;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppStart { // 스트링부트 시작 [ !! 컴포넌트 스캔 ]
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class);
    }
}
