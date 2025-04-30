package bg.deliev.quasarapp.service.aop;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

    @WarnIfExecutionExceeds(timeInMillis = 100)
    public void slowMethod() throws InterruptedException {
        Thread.sleep(150);
    }

    @WarnIfExecutionExceeds(timeInMillis = 200)
    public void fastMethod() throws InterruptedException {
        Thread.sleep(50);
    }
}