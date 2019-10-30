package com.ajacker.searchengine.dao;

import com.ajacker.searchengine.pojo.JobInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ajacker
 * @date 2019/10/27 23:00
 */
@Repository
public interface IJobInfoDao extends ElasticsearchRepository<JobInfo, String> {


    Page<JobInfo> findBySalaryMinGreaterThanAndSalaryMaxLessThanAndExpMinGreaterThanAndExpMaxLessThanAndJobInfoLikeAndJobNameLikeAndJobAddrLike(int salaryMin, int salaryMax, int expMin, int expMax, String keyword, String keyword1, String place, Pageable of);

    Page<JobInfo> findByJobNameLike(String keyword, Pageable of);
}
