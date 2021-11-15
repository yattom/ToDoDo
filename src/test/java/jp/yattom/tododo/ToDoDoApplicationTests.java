package jp.yattom.tododo;

import jp.yattom.tododo.todo.ToDo;
import jp.yattom.tododo.todo.ToDoRepository;
import jp.yattom.tododo.todo.ToDoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@Transactional
class ToDoDoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private ToDoService sut;

	@Autowired
	private ToDoRepository repo;

	@Test
	public void ToDoを書き込む() {
		// 準備
		ToDo toDo = new ToDo("Read the document");

		// 実行
		sut.saveToDo(toDo);

		// 検証
		List<ToDo> actual = sut.findAllToDo();
		assertEquals(1, actual.size());
		assertEquals("Read the document", actual.get(0).getLabel());
	}

}
