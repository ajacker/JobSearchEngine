package com.ajacker.searchengine.config;

import com.ajacker.searchengine.spider.MyProxyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * @author ajacker
 * @date 2019/10/29 10:01
 */
@Configuration
public class SpiderConfig {
    @Autowired
    private MyProxyProvider proxyProvider;

    @Bean
    public HttpClientDownloader httpClientDownloader() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(proxyProvider);

        return httpClientDownloader;
    }
}
