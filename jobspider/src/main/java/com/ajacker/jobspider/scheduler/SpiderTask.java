package com.ajacker.jobspider.scheduler;

import com.ajacker.jobspider.controller.WebSocketServer;
import com.ajacker.jobspider.pojo.MsgInfo;
import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import com.alibaba.fastjson.JSON;
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
        String startTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date());
        //推送消息到网页
        String msg = JSON.toJSONString(new MsgInfo(1, "[计划]爬虫在 " + startTime + " 开始执行..."));
        WebSocketServer.sendInfo(msg, null);

        log.info("第{}次爬虫任务在时间：{} 开始执行...", ++i, startTime);
        //延迟终止爬虫
        applicationTaskExecutor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(executeTime);
            } catch (InterruptedException e) {
                log.error("定时爬虫终止异常！", e);
            }
            String endTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").format(new Date());
            String emsg = JSON.toJSONString(new MsgInfo(1, "[计划]爬虫在 " + endTime + " 停止执行..."));
            WebSocketServer.sendInfo(emsg, null);
            statusMXBean.stop();
        });
    }
}
