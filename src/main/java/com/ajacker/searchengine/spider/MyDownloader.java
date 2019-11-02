package com.ajacker.searchengine.spider;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.downloader.HttpClientRequestContext;
import us.codecraft.webmagic.downloader.HttpUriRequestConverter;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.CharsetUtils;
import us.codecraft.webmagic.utils.HttpClientUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ajacker
 */
@Slf4j
@ThreadSafe
@Component
public class MyDownloader extends AbstractDownloader {
    private final Map<String, CloseableHttpClient> httpClients = new HashMap();
    private final MyProxyProvider proxyProvider;
    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
    private HttpUriRequestConverter httpUriRequestConverter = new HttpUriRequestConverter();
    private boolean responseHeader = true;

    public MyDownloader(MyProxyProvider proxyProvider) {
        this.proxyProvider = proxyProvider;
    }

    public void setHttpUriRequestConverter(HttpUriRequestConverter httpUriRequestConverter) {
        this.httpUriRequestConverter = httpUriRequestConverter;
    }

    private CloseableHttpClient getHttpClient(Site site) {
        if (site == null) {
            return this.httpClientGenerator.getClient(null);
        } else {
            String domain = site.getDomain();
            CloseableHttpClient httpClient = this.httpClients.get(domain);
            if (httpClient == null) {
                synchronized (this) {
                    httpClient = this.httpClients.get(domain);
                    if (httpClient == null) {
                        httpClient = this.httpClientGenerator.getClient(site);
                        this.httpClients.put(domain, httpClient);
                    }
                }
            }

            return httpClient;
        }
    }

    @Override
    public Page download(Request request, Task task) {
        if (task != null && task.getSite() != null) {
            CloseableHttpResponse httpResponse = null;
            CloseableHttpClient httpClient = this.getHttpClient(task.getSite());
            Proxy proxy = this.proxyProvider != null ? this.proxyProvider.getProxy(task) : null;
            HttpClientRequestContext requestContext = this.httpUriRequestConverter.convert(request, task.getSite(), proxy);
            Page page = Page.fail();

            Page res;
            try {
                httpResponse = httpClient.execute(requestContext.getHttpUriRequest(), requestContext.getHttpClientContext());
                page = this.handleResponse(request, request.getCharset() != null ? request.getCharset() : task.getSite().getCharset(), httpResponse, task);
                this.onSuccess(request);
                log.info("以下页面下载成功: {}", request.getUrl());
                return page;
            } catch (Exception e) {
                page.setDownloadSuccess(false);
                log.warn("页面: {} 下载失败，放入重试队列", request.getUrl());
                this.onError(request);
                res = page;
            } finally {
                if (httpResponse != null) {
                    EntityUtils.consumeQuietly(httpResponse.getEntity());
                }

                if (this.proxyProvider != null && proxy != null) {
                    this.proxyProvider.returnProxy(proxy, page, task);
                }

            }

            return res;
        } else {
            throw new NullPointerException("task or site can not be null");
        }
    }

    @Override
    public void setThread(int thread) {
        this.httpClientGenerator.setPoolSize(thread);
    }

    protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task) throws IOException {
        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String contentType = httpResponse.getEntity().getContentType() == null ? "" : httpResponse.getEntity().getContentType().getValue();
        Page page = new Page();
        page.setBytes(bytes);
        if (!request.isBinaryContent()) {
            if (charset == null) {
                charset = this.getHtmlCharset(contentType, bytes);
            }

            page.setCharset(charset);
            page.setRawText(new String(bytes, charset));
        }

        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        page.setStatusCode(httpResponse.getStatusLine().getStatusCode());
        page.setDownloadSuccess(true);
        if (this.responseHeader) {
            page.setHeaders(HttpClientUtils.convertHeaders(httpResponse.getAllHeaders()));
        }
        int code = page.getStatusCode();
        // 状态码判断
        if (HttpStatus.SC_OK <= code && code < HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return page;
        } else {
            log.warn("下载[{}]错误, 响应码: {}, 不在给定的范围内[{}-{})", request.getUrl(), code, HttpStatus.SC_OK, HttpStatus.SC_INTERNAL_SERVER_ERROR);
            page.setDownloadSuccess(false);
        }
        return page;
    }

    private String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
        String charset = CharsetUtils.detectCharset(contentType, contentBytes);
        if (charset == null) {
            charset = Charset.defaultCharset().name();
            log.warn("Charset autodetect failed, use {} as charset. Please specify charset in Site.setCharset()", Charset.defaultCharset());
        }

        return charset;
    }

    @Override
    protected void onError(Request request) {
        super.onError(request);
        this.proxyProvider.updateProxy();
    }
}
