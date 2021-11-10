package jp.yattom.tododo.monitor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @GetMapping("/monitor")
    public Status monitor() {
        return new Status(0, true);
    }
}
