package com.ajacker.jobspider.service;

import com.ajacker.jobspider.pojo.JobInfo;

/**
 * @author ajacker
 * @date 2019/10/28 1:40
 */
public interface IJobInfoService {
    /**
     * 保存
     *
     * @param jobInfo
     */
    void save(JobInfo jobInfo);

}
