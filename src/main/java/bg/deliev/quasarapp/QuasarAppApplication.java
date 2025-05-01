package bg.deliev.quasarapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuasarAppApplication {

  public static void main(String[] args) {
      SpringApplication.run(QuasarAppApplication.class, args);
    }
}
