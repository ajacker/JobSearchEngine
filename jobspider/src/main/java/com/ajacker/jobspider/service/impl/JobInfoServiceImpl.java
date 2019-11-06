package com.ajacker.jobspider.service.impl;


import com.ajacker.jobspider.dao.IJobInfoDao;
import com.ajacker.jobspider.pojo.JobInfo;
import com.ajacker.jobspider.service.IJobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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

}
