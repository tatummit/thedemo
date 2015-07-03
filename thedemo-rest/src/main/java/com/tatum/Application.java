package com.tatum;

import com.tatum.config.ApplicationConfig;
import com.tatum.config.SchedulerConfig;
import com.tatum.quartz.DemoScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({ApplicationConfig.class, SchedulerConfig.class})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
