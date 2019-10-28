package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.service.IJobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ajacker
 * @date 2019/10/28 1:41
 */
@Service
public class JobInfoServiceImpl implements IJobInfoService {
    @Autowired
    private IJobInfoDao jobInfoDao;

    @Override
    public void save(JobInfo jobInfo) {
        jobInfoDao.save(jobInfo);
    }

    @Override
    public void saveAll(List<JobInfo> jobInfo) {
        jobInfoDao.saveAll(jobInfo);
    }
}
