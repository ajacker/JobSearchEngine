package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.pojo.JobResult;
import com.ajacker.searchengine.pojo.SearchParams;
import com.ajacker.searchengine.pojo.TableJobResult;
import com.ajacker.searchengine.service.IJobInfoService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        int page = params.getPageNumber();
        int size = params.getPageSize();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        QueryBuilder query = QueryBuilders.matchAllQuery();
        //关键字搜索条件
        if (StringUtils.isNotBlank(params.getKeyword())) {
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.termQuery("jobName", params.getKeyword()))
                    .should(QueryBuilders.termQuery("jobInfo", params.getKeyword()))
                    .should(QueryBuilders.termQuery("jobAddr", params.getKeyword()))
                    .should(QueryBuilders.termQuery("companyName", params.getKeyword()))
                    .should(QueryBuilders.termQuery("companyInfo", params.getKeyword()))
                    .should(QueryBuilders.termQuery("companyAddr", params.getKeyword()));

            query = QueryBuilders.boolQuery().must(keywordQuery).must(query);
        }
        //薪资范围条件
        if (StringUtils.isNotBlank(params.getSalary())) {
            String[] part = params.getSalary().split("-");
            int min = (int) (Float.parseFloat(part[0]) * 1000);
            int max = (int) (Float.parseFloat(part[1]) * 1000);
            BoolQueryBuilder salaryRangeQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("salaryMin").gt(min).includeLower(true))
                    .must(QueryBuilders.rangeQuery("salaryMax").lt(max).includeUpper(true));
            query = QueryBuilders.boolQuery().must(salaryRangeQuery).must(query);

        }
        //发布时间条件
        if (params.getTime() != 0) {
            Date to = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(to);
            rightNow.add(Calendar.DAY_OF_YEAR, params.getTime() * -1);
            Date from = rightNow.getTime();
            RangeQueryBuilder timeRangeQuery = QueryBuilders.rangeQuery("time")
                    .format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").timeZone("+08")
                    .gt(from)
                    .lt(to);
            query = QueryBuilders.boolQuery().must(timeRangeQuery).must(query);
        }
        //构建查询语句
        queryBuilder.withQuery(query);
        jobInfoDao.search(queryBuilder.build());
        //设置分页
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        Page<JobInfo> pages = jobInfoDao.search(queryBuilder.build());
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
