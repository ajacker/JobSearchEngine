package com.ajacker.jobspider.spider.monitor;

import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

/**
 * @author ajacker
 * @date 2019/11/6 11:26
 */
public interface MyStatusMXBean extends SpiderStatusMXBean {
    /**
     * 页面分析的时候出错的数量
     *
     * @return 页面分析的时候出错的数量
     */
    int getAnalyseErrorPageCount();

    /**
     * 页面分析成功的数量
     *
     * @return 页面分析成功的数量
     */
    int getAnalyseSuccessPageCount();

    /**
     * 页面下载成功的数量
     *
     * @return 页面下载成功的数量
     */
    int getDownloadErrorPageCount();

    /**
     * 页面下载失败的数量
     *
     * @return 页面下载失败的数量
     */
    int getDownloadSuccessPageCount();
}
