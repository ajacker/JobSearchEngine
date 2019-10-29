package com.ajacker.searchengine.spider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;

/**
 * @author ajacker
 * @date 2019/10/29 11:24
 */
@Slf4j
@Component
//TODO: 重写这个类
public class MyDownloader extends HttpClientDownloader {

    @Autowired
    public MyDownloader(MyProxyProvider proxyProvider) {
        super();
        super.setProxyProvider(proxyProvider);
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = null;
        try {
            page = super.download(request, task);
        } catch (Exception e) {
            super.onError(request);
        }
        return page;
    }
}
