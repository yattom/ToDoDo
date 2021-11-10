package jp.yattom.tododo.e2e;

import jp.yattom.tododo.monitor.Status;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HappyPathTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    public class アプリ死活監視 {
        @Test
        public void 生きている() throws Exception {
            assertThat(restTemplate.getForObject("http://localhost:" + port + "/monitor", Status.class).isAlive()).isTrue();
        }
    }
}
