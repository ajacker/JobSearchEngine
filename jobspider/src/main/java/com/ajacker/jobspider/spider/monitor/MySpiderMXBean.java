package com.ajacker.jobspider.spider.monitor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatus;

/**
 * @author ajacker
 * @date 2019/11/6 11:24
 */

public class MySpiderMXBean extends SpiderStatus implements MyStatusMXBean {

    public MySpiderMXBean(Spider spider, SpiderMonitor.MonitorSpiderListener monitorSpiderListener) {
        super(spider, monitorSpiderListener);
    }

    @Override
    public String getSchedulerName() {
        return spider.getScheduler().getClass().getName();
    }
}
