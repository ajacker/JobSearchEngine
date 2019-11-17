package com.ajacker.jobspider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import us.codecraft.webmagic.Spider;

/**
 * @author ajacker
 */
@SpringBootApplication
@EnableScheduling
public class JobSpiderApplication implements CommandLineRunner {

    public static String[] args;
    public static ConfigurableApplicationContext context;

    @Autowired
    private Spider spider;

    public static void main(String[] args) {
        JobSpiderApplication.args = args;
        JobSpiderApplication.context = SpringApplication.
                run(JobSpiderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        spider.run();
    }


}
