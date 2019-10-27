package com.ajacker.searchengine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ajacker
 */
@SpringBootApplication
@EnableScheduling
public class SearchEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchEngineApplication.class, args);
    }

}
