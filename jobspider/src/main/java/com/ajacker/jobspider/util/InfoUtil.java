package com.ajacker.jobspider.util;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ajacker
 * @date 2019/11/7 20:17
 */
@Component
@Getter
public class InfoUtil {
    private final AtomicInteger analyseError = new AtomicInteger(0);
    private final AtomicInteger analyseSuccess = new AtomicInteger(0);

    public void analyseError() {
        analyseError.incrementAndGet();
    }

    public void analyseSuccess() {
        analyseSuccess.incrementAndGet();
    }
}
