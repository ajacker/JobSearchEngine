package com.ajacker.jobspider.scheduler;

import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ajacker
 * @date 2019/11/10 12:08
 */
@Component
@Slf4j
public class SpiderTask implements Runnable {

    @Autowired
    private MyStatusMXBean statusMXBean;
    @Autowired
    private ThreadPoolTaskExecutor applicationTaskExecutor;

    private int i;
    private int executeTime;

    public void setExecuteTime(int executeTime) {
        this.executeTime = executeTime;
    }

    @Override
    public void run() {
        try {
            statusMXBean.start();
        } catch (Exception e) {
            log.error("任务开启出错：", e);
        }
        log.info("第{}次爬虫任务在时间：{} 开始执行...", ++i, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date()));
        //延迟终止爬虫
        applicationTaskExecutor.execute(() -> {
            try {
                TimeUnit.MINUTES.sleep(executeTime);
            } catch (InterruptedException e) {
                log.error("定时爬虫终止异常！", e);
            }
            statusMXBean.stop();
        });
    }
}
