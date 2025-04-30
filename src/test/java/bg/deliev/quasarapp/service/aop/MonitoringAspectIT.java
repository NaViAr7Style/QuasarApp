package bg.deliev.quasarapp.service.aop;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MonitoringAspectIT {

    @Autowired
    private SampleService sampleService;

    @Test
    void shouldLogWarningWhenExecutionExceedsThreshold() throws InterruptedException {
        LogCaptor logCaptor = LogCaptor.forClass(MonitoringAspect.class);

        sampleService.slowMethod();

        List<String> warnLogs = logCaptor.getWarnLogs();

        assertThat(warnLogs)
            .anyMatch(log -> log.contains("ran for") && log.contains("more than the expected"));
    }

    @Test
    void shouldNotLogWarningWhenExecutionIsFast() throws InterruptedException {
        LogCaptor logCaptor = LogCaptor.forClass(MonitoringAspect.class);

        sampleService.fastMethod();

        assertThat(logCaptor.getWarnLogs()).isEmpty();
    }
}