package com.ajacker.jobspider;

import com.ajacker.jobspider.spider.JobProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ajacker
 */
@SpringBootApplication
public class JobSpiderApplication implements CommandLineRunner {

    @Autowired
    private JobProcessor jobProcessor;

    public static void main(String[] args) {
        SpringApplication.run(JobSpiderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobProcessor.process();
    }
}
