package io.knifer.moghostwebui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MoGhostWebuiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoGhostWebuiApplication.class, args);
    }

}
