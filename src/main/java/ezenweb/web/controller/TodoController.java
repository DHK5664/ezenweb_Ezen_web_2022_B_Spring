package ezenweb.web.controller;

import ezenweb.web.domain.member.MemberDto;
import ezenweb.web.domain.todo.TodoDto;
import ezenweb.web.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/todo")
@CrossOrigin(origins = "http://192.168.17.24:3000") // 해당 컨트롤러는 http://localhost:3000 요청 CORS 정책 적용
public class TodoController {
    @Autowired
    TodoService todoService;

    @GetMapping("")
    public List<TodoDto> get(){  // TodoDto , 서비스 , 리포지토리 , 엔티티 작업
        List<TodoDto> result = todoService.get();
        return result;
    }
    @PostMapping("")public boolean post(@RequestBody TodoDto todoDto){
        // [과제] 서비스 구현
        boolean result = todoService.post(todoDto);
        return result;
    }
    @PutMapping("")public boolean put(@RequestBody TodoDto todoDto){
        // [과제] 서비스 구현
        boolean result = todoService.put(todoDto);
        return result;
    }
    @DeleteMapping("")public boolean delete(@RequestParam int id ){
        boolean result = todoService.delete(id);
        // [과제] 서비스 구현
        return true;
    }
}
// 테스트 [ 서비스에게 전달받은 리스트 라는 가정 ]
/*      List<TodoDto> list = new ArrayList<>();
        list.add(new TodoDto(1,"게시물1",true));
        list.add(new TodoDto(2,"게시물2",true));
        list.add(new TodoDto(3,"게시물3",true));
        // [과제] 서비스 구현
        return list; // 서비스에서 리턴 결과를 axios에게 응답
 */