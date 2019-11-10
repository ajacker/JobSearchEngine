package com.ajacker.jobspider.controller;

import com.ajacker.jobspider.pojo.MsgInfo;
import com.ajacker.jobspider.pojo.SpiderInfo;
import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ajacker
 * @date 2019/11/6 13:46
 */
@RestController
public class SpiderController {


    @Autowired
    private SpiderInfo spiderInfo;
    @Autowired
    private MyStatusMXBean statusMXBean;

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public SpiderInfo getLeftPageCount() {
        spiderInfo.updateInfo();
        return spiderInfo;
    }


    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public MsgInfo startSpider() {
        MsgInfo info;
        if ("Running".equals(statusMXBean.getStatus())) {
            info = new MsgInfo(0, "爬虫已经启动！");
        } else {
            try {
                statusMXBean.start();
                info = new MsgInfo(1, "爬虫启动成功！");
            } catch (Exception e) {
                info = new MsgInfo(-1, "爬虫启动失败！");
            }
        }
        return info;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public MsgInfo stopSpider() {
        MsgInfo info;
        if ("Stopped".equals(statusMXBean.getStatus())) {
            info = new MsgInfo(0, "爬虫未在运行！");
        } else {
            try {
                statusMXBean.stop();
                info = new MsgInfo(1, "爬虫停止成功！");
            } catch (Exception e) {
                info = new MsgInfo(-1, "爬虫停止失败！");
            }
        }
        return info;
    }

    //TODO 清空爬虫队列
}
