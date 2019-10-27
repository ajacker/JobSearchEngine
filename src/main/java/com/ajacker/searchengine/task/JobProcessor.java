package com.ajacker.searchengine.task;

import com.ajacker.searchengine.pojo.JobInfo;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Arrays;
import java.util.List;

/**
 * @author ajacker
 * @date 2019/10/27 22:18
 */
@Component
public class JobProcessor implements PageProcessor {
    private static String url = "https://search.51job.com/list/000000,000000,0000,00,9,99,%2520,2,1.html";
    private Site site = Site.me()
            .setCharset("gbk")
            .setRetryTimes(3)
            .setRetrySleepTime(1000)
            .setSleepTime(1000);

    public static void main(String[] args) {
        Spider.create(new JobProcessor())
                .addUrl(url)
                .setScheduler(new QueueScheduler())
                .thread(1)
                .run();
    }

    @Override
    public void process(Page page) {
        //解析页面，获取招聘信息的url地址
        List<Selectable> list = page.getHtml().css("div#resultList > div.el:not(.title)").nodes();

        //判断是详情页还是列表页
        if (list.size() != 0) {
            //列表页
            for (Selectable selectable : list) {
                //获取url
                String jobInfoUrl = selectable.links().toString();
                //加入任务队列
                page.addTargetRequest(jobInfoUrl);
            }
            //获取下一页url
            String nextPageUrl = page.getHtml().css("#rtNext").links().toString();
            //添加到任务队列
            page.addTargetRequest(nextPageUrl);
        } else {
            //如果是详情页，爬取数据
            this.saveJobInfo(page);
        }
    }

    private void saveJobInfo(Page page) {
        //准备要封装的对象
        JobInfo jobInfo = new JobInfo();
        Html html = page.getHtml();
        //获取数据
        //公司名称
        jobInfo.setCompanyName(html.css("div.cn p.cname a", "text").toString());
        //公司地址
        jobInfo.setCompanyAddr(html.css("div.bmsg > p.fp", "text").toString());
        //公司信息
        jobInfo.setCompanyInfo(html.css("div.tmsg").xpath("tidyText()").toString().trim());
        //工作名称
        jobInfo.setJobName(html.css("div.cn > h1", "text").toString());
        //工作地点
        String data = html.css("p.msg.ltype", "title").toString();
        String[] infos = data.split("\\|");
        jobInfo.setJobAddr(infos[0].trim());
        //工作信息
        jobInfo.setJobInfo(html.css("div.job_msg").xpath("tidyText()").toString().trim());
        //地址
        jobInfo.setUrl(page.getUrl().toString());
        //薪资范围（k）
        String sal = html.css("div.cn > strong", "text").toString();
        String[] part = sal.split("/");
        int min, max;
        if (part[0].endsWith("万")) {
            String[] range = part[0].replace("万", "").split("-");
            min = (int) (Float.parseFloat(range[0]) * 10 * 1000);
            max = (int) (Float.parseFloat(range[1]) * 10 * 1000);
        } else {
            String[] range = part[0].replace("千", "").split("-");
            try {
                min = (int) (Float.parseFloat(range[0]) * 1000);
                max = (int) (Float.parseFloat(range[1]) * 1000);
            } catch (NumberFormatException e) {
                min = 0;
                max = 2000;
            }
        }
        if ("年".equals(part[1])) {
            min /= 10;
            max /= 10;
        }
        jobInfo.setSalaryMin(min);
        jobInfo.setSalaryMax(max);
        //发布时间
        Arrays.stream(infos).filter(s -> s.contains("发布")).findFirst().ifPresent(
                t -> jobInfo.setTime(t.replace("发布", "")
                        .replaceAll("\u00a0", ""))
        );
        //工作经验
        String exp = infos[1].replaceAll("\u00a0", "");
        int expMin, expMax;
        if (exp.startsWith("无")) {
            expMin = 0;
            expMax = 100;
        } else {
            exp = exp.replace("年经验", "");
            if (exp.contains("-")) {
                String[] range = exp.split("-");
                expMin = Integer.parseInt(range[0]);
                expMax = Integer.parseInt(range[1]);
            } else {
                try {
                    expMin = Integer.parseInt(exp);
                    expMax = Integer.parseInt(exp);
                } catch (NumberFormatException e) {
                    expMin = 10;
                    expMax = 100;
                }
            }
        }
        jobInfo.setExpMin(expMin);
        jobInfo.setExpMax(expMax);
        System.out.println(jobInfo);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
