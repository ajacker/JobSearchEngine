package com.ajacker.jobspider.scheduler;

import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author ajacker
 * @date 2019/11/10 11:01
 */
@Slf4j
@Component
@EnableScheduling
@Lazy(false)
public class SpiderScheduler implements SchedulingConfigurer {

    public static String cron = "0/30 * * * * ?";
    @Autowired
    private MyStatusMXBean statusMXBean;
    @Autowired
    private ThreadPoolTaskExecutor applicationTaskExecutor;
    public static int executeTime = 20;
    private int i = 0;

    /**
     * 设置定时任务的cron表达式
     *
     * @param cron cron表达式
     */
    public static void setCron(final String cron) {
        SpiderScheduler.cron = cron;
    }

    /**
     * 设置执行时间 单位分钟 到了时间会stop爬虫
     *
     * @param time 执行时间 单位分钟
     */
    public static void setExecuteTime(final int time) {
        SpiderScheduler.executeTime = time;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(() -> {
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
        }, triggerContext -> {
            //任务触发，可修改任务的执行周期   cron改变了，这个会立马生效
            CronTrigger trigger = new CronTrigger(cron);
            return trigger.nextExecutionTime(triggerContext);
        });
    }
}
