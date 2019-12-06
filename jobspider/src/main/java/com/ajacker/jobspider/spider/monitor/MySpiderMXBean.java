package com.ajacker.jobspider.spider.monitor;

import com.ajacker.jobspider.util.InfoUtil;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatus;

/**
 * @author ajacker
 * @date 2019/11/6 11:24
 */

public class MySpiderMXBean extends SpiderStatus implements MyStatusMXBean {
    private InfoUtil infoUtil;

    public MySpiderMXBean(Spider spider, SpiderMonitor.MonitorSpiderListener monitorSpiderListener, InfoUtil infoUtil) {
        super(spider, monitorSpiderListener);
        this.infoUtil = infoUtil;
    }


    @Override
    public int getTotalPageCount() {
        return this.getDownloadSuccessPageCount() + this.getDownloadErrorPageCount();
    }


    @Override
    public int getAnalyseErrorPageCount() {
        return infoUtil.getAnalyseError().intValue();
    }

    @Override
    public int getAnalyseSuccessPageCount() {
        return getDownloadSuccessPageCount() - getAnalyseErrorPageCount();
    }

    @Override
    public int getDownloadErrorPageCount() {
        return super.getErrorPageCount();
    }

    @Override
    public int getDownloadSuccessPageCount() {
        return super.getSuccessPageCount();
    }
}
