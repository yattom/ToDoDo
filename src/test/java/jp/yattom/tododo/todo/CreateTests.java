package jp.yattom.tododo.todo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@Transactional
public class CreateTests {
    @Nested
    public class 結合テスト {
        @Autowired
        private ToDoController sut;

        @Autowired
        private ToDoRepository repo;

        // テスト用のヘルパー
        // ToDoをDB上に作成する
        void prepareToDos(String... labels) {
            for(int i = 0; i < labels.length; i++) {
                ToDo todo = new ToDo();
                todo.setLabel(labels[i]);
                repo.save(todo);
            }
        }

        @Test
        public void 新規保存できる() {
            // 準備
            ToDo target = new ToDo();
            target.setLabel("My ToDo");

            // 実行
            sut.createToDo(target);

            // 検証
            List<ToDo> actual = repo.findAll();
            Assertions.assertEquals(1, actual.size());
            Assertions.assertEquals("My ToDo", actual.get(0).getLabel());
        }

        /*
         * 下にある「Serviceテスト.同じラベルのToDoは登録できない」 と同じ意図。
         * 結合テストで実装するならば、DBにテストデータを作成する。
         * 下のService単体でのテストの場合はモックを使う。
         */
        @Nested
        public class 同じラベルのToDoは登録できない {
            @Test
            public void 重複する場合() {
                prepareToDos("Duplicated Todo");
                ToDo target = new ToDo();
                target.setLabel("Duplicated ToDo");
                sut.createToDo(target);
                Assertions.assertThrows(IllegalStateException.class, () -> {
                    sut.createToDo(target);
                });
            }

            @Test
            public void 重複しない場合() {
                ToDo target = new ToDo();
                target.setLabel("Non Duplicated ToDo");
                Assertions.assertDoesNotThrow(() -> {
                    sut.createToDo(target);
                });
            }
        }
    }

    @Nested
    public class Serviceテスト {
        @Autowired
        private ToDoService sut;

        @MockBean
        private ToDoRepository repo;

        // この例では、既存データを用いたvalidationを、Serviceで実装している。
        // もし実プロジェクトでControllerに実装しているのであれば、それに合わせて直したい。
        @Nested
        public class 同じラベルのToDoは登録できない {
            @Test
            public void 重複する場合() {
                when(repo.countByLabel(any())).thenReturn(1);
                ToDo target = new ToDo();
                target.setLabel("Duplicated ToDo");
                Assertions.assertFalse(sut.isNotDuplicate(target));
            }

            @Test
            public void 重複しない場合() {
                when(repo.countByLabel(any())).thenReturn(0);
                ToDo target = new ToDo();
                target.setLabel("Non Duplicated ToDo");
                Assertions.assertTrue(sut.isNotDuplicate(target));
            }
        }
    }
}
