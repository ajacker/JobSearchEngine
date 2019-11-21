package com.ajacker.jobspider.config;

import com.ajacker.jobspider.spider.ElasticSearchPipeLine;
import com.ajacker.jobspider.spider.JobProcessor;
import com.ajacker.jobspider.spider.MyDownloader;
import com.ajacker.jobspider.spider.MyRedisScheduler;
import com.ajacker.jobspider.spider.monitor.MySpiderMXBean;
import com.ajacker.jobspider.util.InfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

import javax.management.JMException;

/**
 * @author ajacker
 * @date 2019/11/7 20:34
 */
@Configuration
@Slf4j
@PropertySource("classpath:spider.properties")
public class SpiderConfig {
    @Value("${spider.UUID}")
    private String UUID;
    @Value("${spider.startUrl}")
    private String url;
    @Value("${spider.threadNum}")
    private int threadNum;
    @Value("${spider.sleepTime}")
    private int sleepTime;
    @Autowired
    private InfoUtil infoUtil;
    @Autowired
    private MyDownloader downloader;
    @Autowired
    private MyRedisScheduler scheduler;
    @Autowired
    private ElasticSearchPipeLine elasticSearchPipeLine;
    @Autowired
    private JobProcessor jobProcessor;

    @Bean
    public Spider spider() {
        log.info("线程数：{},爬虫间隔：{}ms", threadNum, sleepTime);
        return Spider.create(jobProcessor)
                .setUUID(UUID)
                .addUrl(url)
                .setScheduler(scheduler)
                .addPipeline(elasticSearchPipeLine)
                .setDownloader(downloader)
                .thread(threadNum);
    }

    @Bean
    public Site site() {
        return Site.me()
                .setCharset("gbk")
                .setCycleRetryTimes(30)
                .setRetrySleepTime(500)
                .setSleepTime(sleepTime);
    }

    @Bean
    public SpiderMonitor spiderMonitor(Spider spider) {
        SpiderMonitor spiderMonitor = new SpiderMonitor() {
            @Override
            protected SpiderStatusMXBean getSpiderStatusMBean(Spider spider, MonitorSpiderListener monitorSpiderListener) {
                return new MySpiderMXBean(spider, monitorSpiderListener, infoUtil);
            }
        };
        try {
            spiderMonitor.register(spider);
        } catch (JMException e) {
            log.error("爬虫监视器创建失败", e);
        }
        return spiderMonitor;
    }

}
