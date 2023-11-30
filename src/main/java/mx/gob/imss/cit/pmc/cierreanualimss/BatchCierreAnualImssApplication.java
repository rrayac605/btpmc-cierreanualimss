package mx.gob.imss.cit.pmc.cierreanualimss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@EnableScheduling
public class BatchCierreAnualImssApplication {

    public static void main(String[] args) {
        SpringApplication.run(BatchCierreAnualImssApplication.class, args);
    }

}
