package com.ajacker.jobspider.controller;

import com.ajacker.jobspider.scheduler.SpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ScheduledFuture;

/**
 * @author ajacker
 * @date 2019/11/10 12:07
 */
@RestController
public class DynamicTaskController {

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private SpiderTask spiderTask;

    /**
     * 在ScheduledFuture中有一个cancel可以停止定时任务。
     */
    private ScheduledFuture<?> future;


    /**
     * 启动任务
     **/
    @RequestMapping("/startTask")
    public String startCron(String cron, int time) {
        stopCron();
        spiderTask.setExecuteTime(time);
        future = taskScheduler.schedule(spiderTask, new CronTrigger(cron));
        return "startTask";
    }

    /**
     * 启此任务
     **/
    @RequestMapping("/stopTask")
    public String stopCron() {
        if (future != null) {
            future.cancel(true);
        }
        return "stopTask";
    }


}
