package jp.yattom.tododo.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ToDoService {
    final ToDoRepository repository;

    @Autowired
    public ToDoService(ToDoRepository repository) {
        this.repository = repository;
    }

    public List<ToDo> findAllToDo() {
        return repository.findAll();
    }

    public void saveToDo(ToDo target) {
        repository.save(target);
    }
}
