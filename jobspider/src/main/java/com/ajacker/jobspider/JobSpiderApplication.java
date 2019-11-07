package com.ajacker.jobspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import us.codecraft.webmagic.Spider;

/**
 * @author ajacker
 */
@SpringBootApplication
public class JobSpiderApplication implements CommandLineRunner {


    @Autowired
    private Spider spider;

    public static void main(String[] args) {
        SpringApplication.
                run(JobSpiderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        spider.run();
    }
}
