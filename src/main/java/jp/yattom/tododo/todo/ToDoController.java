package jp.yattom.tododo.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/todos")
public class ToDoController {
    private final ToDoService service;

    public ToDoController(@Autowired ToDoService service) {
        this.service = service;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createToDo(ToDo todo) {
        service.saveToDo(todo);
    }
}
