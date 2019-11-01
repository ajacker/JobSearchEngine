package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.pojo.JobResult;
import com.ajacker.searchengine.pojo.SearchParams;
import com.ajacker.searchengine.pojo.TableJobResult;
import com.ajacker.searchengine.service.IJobInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public TableJobResult search(SearchParams params) {
        //TODO:这里不合适 还得改 只是测试用
        String keyword = params.getKeyword();
        int page = params.getPageNumber();
        int size = params.getPageSize();

        Page<JobInfo> pages = jobInfoDao.findByJobNameLikeOrJobInfoLikeOrJobAddrLike(keyword,
                keyword, keyword, PageRequest.of(page - 1, size));
        TableJobResult tableJobResult = new TableJobResult();
        //设置总结果数量
        tableJobResult.setTotal(pages.getTotalElements());
        //封装结果
        List<JobResult> rows = pages.getContent().stream().map(jobInfo -> {
            JobResult jobResult = new JobResult();
            BeanUtils.copyProperties(jobInfo, jobResult);
            jobResult.setSalary(String.format("%dk-%dk", jobInfo.getSalaryMin() / 1000, jobInfo.getSalaryMax() / 1000));
            DateFormat dateFormat = new SimpleDateFormat("MM-dd");
            Date date = jobInfo.getTime();
            jobResult.setTime(dateFormat.format(date));
            return jobResult;
        }).collect(Collectors.toList());
        tableJobResult.setRows(rows);
        return tableJobResult;


    }
}
