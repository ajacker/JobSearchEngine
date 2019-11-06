package com.ajacker.jobspider.util;

import org.junit.Test;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author ajacker
 * @date 2019/11/6 14:14
 */
public class JMXUtil {
    @Test
    public void init() throws Exception {
        String host = "127.0.0.1";
        int port = 1234;
        String url = "service:jmx:rmi:///jndi/rmi://" + host + ":" + port + "/jmxrmi";

        JMXServiceURL serviceURL = new JMXServiceURL(url);
        final JMXConnector connector;
        try {
            connector = JMXConnectorFactory.connect(serviceURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        MBeanServerConnection connection = connector.getMBeanServerConnection();
        ObjectName oName = new ObjectName("WebMagic", "name", "JobSpider");

        MBeanInfo mBeanInfo = connection.getMBeanInfo(oName);
        System.out.println("属性：");
        MBeanAttributeInfo[] attributes = mBeanInfo.getAttributes();
        System.out.println(Arrays.stream(attributes).map(MBeanFeatureInfo::getName)
                .collect(Collectors.joining(",")));
        System.out.println("方法：");
        MBeanOperationInfo[] operations = mBeanInfo.getOperations();
        System.out.println(Arrays.stream(operations).map(MBeanFeatureInfo::getName)
                .collect(Collectors.joining(",")));
    }
}
