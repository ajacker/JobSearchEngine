package com.ajacker.jobspider.controller;

import com.ajacker.jobspider.scheduler.SpiderTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
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
    public Map<String, String> startCron(String cron, int time) {
        Map<String, String> map = new HashMap<>();
        spiderTask.setExecuteTime(time);
        if (time > 0) {
            try {
                future = taskScheduler.schedule(spiderTask, new CronTrigger(cron));
                map.put("status", "1");
                map.put("message", "定时任务开启成功！cron表达式：" + cron);
            } catch (Exception e) {
                map.put("status", "-1");
                map.put("message", "cron表达式格式错误！错误信息：" + e.getMessage());
            }
        } else {
            map.put("status", "0");
            map.put("message", "爬虫运行的持续时间需大于0");
        }
        return map;
    }

    /**
     * 启此任务
     **/
    @RequestMapping("/stopTask")
    public Map<String, String> stopCron() {
        Map<String, String> map = new HashMap<>();
        if (future != null && !future.isCancelled()) {
            future.cancel(true);
            map.put("status", "1");
            map.put("message", "定时任务停止成功！");
        } else {
            map.put("status", "0");
            map.put("message", "定时任务未在运行中！");
        }
        return map;
    }


}
