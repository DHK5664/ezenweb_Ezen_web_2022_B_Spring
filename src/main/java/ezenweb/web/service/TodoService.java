package ezenweb.web.service;

import ezenweb.example.day04.domain.entity.product.ProductEntity;
import ezenweb.web.domain.todo.TodoDto;
import ezenweb.web.domain.todo.TodoEntity;
import ezenweb.web.domain.todo.TodoEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TodoService {
    @Autowired
    TodoEntityRepository todoEntityRepository;

    @Transactional
    public boolean post(TodoDto todoDto){
        TodoEntity entity = todoEntityRepository.save(todoDto.todoEntity());
        if(entity.getId() <1){return false;}
        else{return true;}
    }
    @Transactional
    public List<TodoDto> get(){
        List<TodoEntity> entityList = todoEntityRepository.findAll();
        ArrayList<TodoDto> list = new ArrayList<>();
        entityList.forEach(e->{
            list.add(e.todoDto());
        });
        return list;
    }
    @Transactional
    public boolean put(TodoDto todoDto){
        Optional<TodoEntity> optionalTodoEntity = todoEntityRepository.findById(todoDto.getId());
        if(optionalTodoEntity.isPresent()){
            TodoEntity todoEntity = optionalTodoEntity.get();
            todoEntity.setTitle(todoDto.getTitle());
            todoEntity.setDone(todoDto.isDone());
            return true;
        }
        return false;
    }
    @Transactional
    public boolean delete(int id){
        Optional<TodoEntity> optionalTodoEntity = todoEntityRepository.findById(id);
        if(optionalTodoEntity.isPresent()){
            TodoEntity entity = optionalTodoEntity.get();
            todoEntityRepository.delete(entity);
            return true;
        }
        return false;
    }

}
