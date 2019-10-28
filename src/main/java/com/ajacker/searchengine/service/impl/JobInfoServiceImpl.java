package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.service.IJobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Override
    public void search(String keyword, String salary, String time, String education, String exp, String place) {
        //TODO:这里不合适 还得改 只是测试用
        String[] salaryParts = salary.split("-");
        int salaryMin = (int) (Float.parseFloat(salaryParts[0]) * 1000);
        int salaryMax = (int) (Float.parseFloat(salaryParts[1]) * 1000);
        String[] expParts = exp.split("-");
        int expMin = Integer.parseInt(expParts[0]);
        int expMax = Integer.parseInt(expParts[1]);

        salaryMin = 0;
        salaryMax = 10000000;

        int page = 1;
        Page<JobInfo> pages = jobInfoDao.findBySalaryMinGreaterThanAndSalaryMaxLessThanAndExpMinGreaterThanAndExpMaxLessThanAndJobInfoLikeAndJobNameLikeAndJobAddrLike(salaryMin,
                salaryMax, expMin, expMax, keyword, keyword, place, PageRequest.of(page - 1, 30));
        ;
        System.out.println(pages.getTotalElements());
        pages = jobInfoDao.findByJobNameLike(keyword, PageRequest.of(page - 1, 30));
        System.out.println(pages.getTotalElements());
        pages.forEach(System.out::println);

    }
}
