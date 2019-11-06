package com.ajacker.jobspider.dao;

import com.ajacker.jobspider.pojo.JobInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ajacker
 * @date 2019/10/27 23:00
 */
@Repository
public interface IJobInfoDao extends ElasticsearchRepository<JobInfo, String> {


}
