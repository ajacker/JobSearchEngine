package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.service.IJobInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ajacker
 * @date 2019/10/28 1:41
 */
@Service
public class JobInfoServiceImpl implements IJobInfoService {
    private final IJobInfoDao jobInfoDao;

    public JobInfoServiceImpl(IJobInfoDao jobInfoDao) {
        this.jobInfoDao = jobInfoDao;
    }


    @Override
    public void save(JobInfo jobInfo) {
        jobInfoDao.save(jobInfo);
    }

    @Override
    public void saveAll(List<JobInfo> jobInfo) {
        jobInfoDao.saveAll(jobInfo);
    }
}
