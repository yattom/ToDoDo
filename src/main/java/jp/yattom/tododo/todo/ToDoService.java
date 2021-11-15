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
        if(!isNotDuplicate(target)) {
            throw new IllegalStateException("duplicated todo");
        }
        repository.save(target);
    }

    public boolean isNotDuplicate(ToDo target) {
        return repository.countByLabel(target.getLabel()) == 0;
    }
}
