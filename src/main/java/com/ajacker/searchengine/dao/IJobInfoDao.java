package com.ajacker.searchengine.dao;

import com.ajacker.searchengine.pojo.JobInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ajacker
 * @date 2019/10/27 23:00
 */
@Repository
public interface IJobInfoDao extends ElasticsearchRepository<JobInfo, String> {


}
