package com.ajacker.jobspider.controller;

import com.ajacker.jobspider.pojo.MsgInfo;
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
    public MsgInfo startCron(String cron, int time) {
        spiderTask.setExecuteTime(time);
        MsgInfo info;
        if (time > 0) {
            try {
                future = taskScheduler.schedule(spiderTask, new CronTrigger(cron));
                info = new MsgInfo(1, "定时任务开启成功！cron表达式：" + cron + "\t每次运行时长：" + time + "分钟");
            } catch (Exception e) {
                info = new MsgInfo(-1, "cron表达式格式错误！错误信息：" + e.getMessage());
            }
        } else {
            info = new MsgInfo(0, "爬虫运行的持续时间需大于0");
        }
        return info;
    }

    /**
     * 启此任务
     **/
    @RequestMapping("/stopTask")
    public MsgInfo stopCron() {
        MsgInfo info;
        if (future != null && !future.isCancelled()) {
            future.cancel(true);
            info = new MsgInfo(1, "定时任务停止成功！");
        } else {
            info = new MsgInfo(0, "定时任务未在运行中！");
        }
        return info;
    }


}
