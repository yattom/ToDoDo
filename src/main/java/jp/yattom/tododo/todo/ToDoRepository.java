package jp.yattom.tododo.todo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDo, Integer> {
    @Override
    <S extends ToDo> S save(S entity);

    @Override
    List<ToDo> findAll();

    @Override
    void deleteAll();
}