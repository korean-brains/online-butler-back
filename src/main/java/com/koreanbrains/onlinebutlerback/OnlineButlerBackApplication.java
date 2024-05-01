package com.koreanbrains.onlinebutlerback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class OnlineButlerBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineButlerBackApplication.class, args);
    }

}
