package ezenweb.example.day03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController     // MVC 모델 컨트롤러
@Slf4j              // 로그
@RequestMapping("/note") // 공동URL

public class NoteController { // 얘는 입구
    @Autowired  // 생성자 자동 주입 [ *단 스프링 컨테이너에 등록이 되어있는 경우만 가능]
    NoteService noteService;

    // ---------------- HTML 반환 -----------------
    @GetMapping("") // http://localhost:8080/note
    public Resource index(){
        return new ClassPathResource("templates/note.html");
    }
    // ---------------RESTful API-----------------
    // 1. 쓰기
    @PostMapping("/write")      // http://localhost:8080/note/write // {" ncontents" : "내용물"}
    public boolean write( @RequestBody NoteDto noteDto){ log.info("write in : " +noteDto);
        // 1. 인스턴스 객체
        // NoteService service = new NoteService();
        // service.write(noteDto);
        // 2. @Autowired
        boolean result = noteService.write(noteDto);
        // * response.getWriter().print() == return
        return result;
    }
    // 2. 출력
    @GetMapping("/get")         // http://localhost:8080/note/get
    public ArrayList<NoteDto> get(){log.info("get in");
        ArrayList<NoteDto> result = noteService.get();
        return result;
    }
    // 3. 삭제
    @DeleteMapping("/delete")   // http://localhost:8080/note/delete?nno=1
    public boolean delete(@RequestParam int nno){log.info("delete in : " + nno);
        boolean result = noteService.delete(nno);
        return result;
    }
    // 4. 수정
    @PutMapping("/update")      // http://localhost:8080/note/update // "nno" : 3 ,  " ncontents" : "내용물"}
    public boolean update(@RequestBody NoteDto dto){ log.info("update in : " + dto);
        boolean result = noteService.update(dto);
        return result;
    }
}
