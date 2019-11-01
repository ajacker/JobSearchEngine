package com.ajacker.searchengine.pojo;

import lombok.Data;

/**
 * @author ajacker
 * @date 2019/11/1 10:19
 * 呈现结果列表的包装类
 */
@Data
public class JobResult {
    private String jobName;
    private String companyName;
    private String jobAddr;
    private String salary;
    private String time;
    private String url;
}
