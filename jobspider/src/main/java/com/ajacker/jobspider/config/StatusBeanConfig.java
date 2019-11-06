package com.ajacker.jobspider.config;

import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author ajacker
 * @date 2019/11/6 15:06
 */
@Configuration
@Slf4j
public class StatusBeanConfig {
    @Bean
    public MyStatusMXBean statusMXBean() throws Exception {
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName oName = new ObjectName("WebMagic", "name", "JobSpider");
        MyStatusMXBean mBean = JMX.newMXBeanProxy(platformMBeanServer, oName, MyStatusMXBean.class, true);
        return mBean;
    }
}
