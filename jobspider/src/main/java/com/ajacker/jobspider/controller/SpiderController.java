package com.ajacker.jobspider.controller;

import com.ajacker.jobspider.pojo.SpiderInfo;
import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> startSpider() {
        Map<String, String> map = new HashMap<>();
        if ("Running".equals(statusMXBean.getStatus())) {
            map.put("status", "0");
            map.put("message", "爬虫已经启动！");
        } else {
            try {
                statusMXBean.start();
                map.put("status", "1");
                map.put("message", "爬虫启动成功！");
            } catch (Exception e) {
                map.put("status", "-1");
                map.put("message", "爬虫启动失败！");
            }
        }
        return map;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public Map<String, String> stopSpider() {
        Map<String, String> map = new HashMap<>();
        if ("Stopped".equals(statusMXBean.getStatus())) {
            map.put("status", "0");
            map.put("message", "爬虫未在运行！");
        } else {
            try {
                statusMXBean.stop();
                map.put("status", "1");
                map.put("message", "爬虫停止成功！");
            } catch (Exception e) {
                map.put("status", "-1");
                map.put("message", "爬虫停止失败！");
            }
        }
        return map;
    }
}
