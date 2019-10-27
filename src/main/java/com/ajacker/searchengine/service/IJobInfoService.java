package com.ajacker.searchengine.service;

import com.ajacker.searchengine.pojo.JobInfo;

import java.util.List;

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

    /**
     * 保存所有
     *
     * @param jobInfo
     */
    void saveAll(List<JobInfo> jobInfo);
}
