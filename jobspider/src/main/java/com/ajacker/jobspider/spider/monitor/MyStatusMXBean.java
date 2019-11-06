package com.ajacker.jobspider.spider.monitor;

import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

/**
 * @author ajacker
 * @date 2019/11/6 11:26
 */
public interface MyStatusMXBean extends SpiderStatusMXBean {
    public String getSchedulerName();
}
