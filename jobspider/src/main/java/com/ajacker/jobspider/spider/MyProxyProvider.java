package com.ajacker.jobspider.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.ProxyProvider;

/**
 * @author ajacker
 * @date 2019/10/29 9:45
 */
@Component
@Slf4j
@PropertySource("classpath:spider.properties")
public class MyProxyProvider implements ProxyProvider {
    @Value("${spider.proxy.url}")
    private String baseUrl;
    private static boolean lastStatus;
    private static Proxy lastProxy;

    @Override
    public void returnProxy(Proxy proxy, Page page, Task task) {
        lastStatus = page.isDownloadSuccess() && page.getStatusCode() == 200;
        if (!lastStatus) {
            RestTemplate restTemplate = new RestTemplate();
            String u = baseUrl + "/delete?proxy=" + proxy.getHost() + ":" + proxy.getPort();
            restTemplate.execute(u, HttpMethod.GET, null, null);
        }
    }

    @Override
    public Proxy getProxy(Task task) {
        if (!lastStatus) {
            updateProxy();
        }
        return lastProxy;
    }

    public void updateProxy() {
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForEntity(baseUrl + "/get", String.class).getBody();
        JSONObject jsonObject = JSON.parseObject(result);
        if (StringUtils.isEmpty(jsonObject.getString("proxy"))) {
            return;
        }
        String[] proxy = jsonObject.getString("proxy").split(":");
        lastProxy = new Proxy(proxy[0], Integer.parseInt(proxy[1]));
        log.info("获得代理：" + lastProxy);
    }
}
