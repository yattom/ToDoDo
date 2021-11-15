package jp.yattom.tododo.todo;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
        // ヘルパーは別クラスに入れるほうがよいかもしれないが、
        // 一方、「このテストケース内だけ」で閉じると、見通しがよい＝コードが読みやすい
        // 共通化したりまとめたり一般化したり、あまりしないように気を付ける
        // ヘルパーを作るときには、テストを書く人が便利になるように考えて設計する
        // テストを書く人自身が必要に応じて作るのがベストで、QAエンジニア(SET)が協力してもよい
        void prepareToDos(String... labels) {
            for (String label : labels) {
                ToDo todo = new ToDo(label);
                repo.save(todo);
            }
        }

        @Test
        public void 新規保存できる() {
            // 準備
            ToDo target = new ToDo("My ToDo");

            // 実行
            sut.createToDo(target);

            // 検証
            List<ToDo> actual = repo.findAll();
            assertEquals(1, actual.size());
            assertEquals("My ToDo", actual.get(0).getLabel());
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
                ToDo target = new ToDo("Duplicated ToDo");
                sut.createToDo(target);
                assertThrows(IllegalStateException.class, () -> {
                    sut.createToDo(target);
                });
            }

            @Test
            public void 重複しない場合() {
                ToDo target = new ToDo("Non Duplicated ToDo");
                assertDoesNotThrow(() -> {
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
                ToDo target = new ToDo("Duplicated ToDo");
                assertFalse(sut.isNotDuplicate(target));
            }

            @Test
            public void 重複しない場合() {
                when(repo.countByLabel(any())).thenReturn(0);
                ToDo target = new ToDo("Non Duplicated ToDo");
                assertTrue(sut.isNotDuplicate(target));
            }
        }
    }
}
