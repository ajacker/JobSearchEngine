package com.ajacker.searchengine.service.impl;

import com.ajacker.searchengine.dao.IJobInfoDao;
import com.ajacker.searchengine.pojo.*;
import com.ajacker.searchengine.service.IJobInfoService;
import com.ajacker.searchengine.util.AreaUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
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
    @Autowired
    private RestTemplate restTemplate;
    @Value("${spring.elasticsearch.rest.uris}")
    private String baseUrl;


    @Override
    public void save(JobInfo jobInfo) {
        jobInfoDao.save(jobInfo);
    }

    @Override
    public void saveAll(List<JobInfo> jobInfo) {
        jobInfoDao.saveAll(jobInfo);
    }


    public TableJobResult advanceSearch(AdvanceSearchParams params) {
        int page = params.getPageNumber();
        int size = params.getPageSize();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();
        //工作名称搜索
        if (StringUtils.isNotBlank(params.getJobNameKeyWord())) {
            if (params.getJobNameType() == 1) {
                //模糊搜索
                MatchQueryBuilder jobNameQuery = QueryBuilders.matchQuery("jobName", params.getJobNameKeyWord());
                query = QueryBuilders.boolQuery().must(jobNameQuery).must(query);
            } else {
                //精确搜索
                MatchPhraseQueryBuilder jobNameQuery = QueryBuilders.matchPhraseQuery("jobName", params.getJobNameKeyWord());
                query = QueryBuilders.boolQuery().must(jobNameQuery).must(query);
            }
        }
        //公司名称搜索
        if (StringUtils.isNotBlank(params.getCompanyNameKeyWord())) {
            if (params.getCompanyNameType() == 1) {
                //模糊搜索
                MatchQueryBuilder companyNameQuery = QueryBuilders.matchQuery("companyName", params.getCompanyNameKeyWord());
                query = QueryBuilders.boolQuery().must(companyNameQuery).must(query);
            } else {
                //精确搜索
                MatchPhraseQueryBuilder companyNameQuery = QueryBuilders.matchPhraseQuery("companyName", params.getCompanyNameKeyWord());
                query = QueryBuilders.boolQuery().must(companyNameQuery).must(query);
            }
        }
        //地址省份搜索
        List<String> provinces = params.getAddressProvence();
        if (CollectionUtils.isNotEmpty(provinces)) {
            BoolQueryBuilder provincesBuilder = new BoolQueryBuilder();
            for (String province : provinces) {
                TermsQueryBuilder temp = QueryBuilders.termsQuery("jobAddr",
                        AreaUtil.province.get(province));
                provincesBuilder = provincesBuilder.should(temp);
            }
            query = QueryBuilders.boolQuery().must(provincesBuilder).must(query);
        }
        //地区额外关键字搜索
        if (StringUtils.isNotBlank(params.getAddressExtra())) {
            BoolQueryBuilder extraQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("jobAddr", params.getAddressExtra()))
                    .should(QueryBuilders.matchQuery("companyAddr", params.getAddressExtra()));
            query = QueryBuilders.boolQuery().must(extraQuery).must(query);

        }
        //薪资范围搜索
        if (params.getMinSalary() != 0 || params.getMaxSalary() != 0) {
            int min = params.getMinSalary() * 1000;
            int max = params.getMaxSalary() * 1000;
            BoolQueryBuilder salaryRangeQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("salaryMin").gt(min).includeLower(true))
                    .must(QueryBuilders.rangeQuery("salaryMax").lt(max).includeUpper(true));
            query = QueryBuilders.boolQuery().must(salaryRangeQuery).must(query);
        }
        //发布时间条件
        if (params.getDateFrom() != null || params.getDateTo() != null) {
            Calendar temp = Calendar.getInstance();
            temp.setTime(params.getDateTo());
            temp.add(Calendar.DAY_OF_YEAR, 1);
            Date to = temp.getTime();
            RangeQueryBuilder timeRangeQuery = QueryBuilders.rangeQuery("time")
                    .format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .from(params.getDateFrom())
                    .to(to);
            query = QueryBuilders.boolQuery().must(timeRangeQuery).must(query);
        }
        //构建查询语句
        queryBuilder.withQuery(query);
        return getTableJobResult(queryBuilder, params.getSortName(), params.getSortOrder(), PageRequest.of(page - 1, size));
    }


    @Override
    public TableJobResult search(SearchParams params) {
        int page = params.getPageNumber();
        int size = params.getPageSize();
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        QueryBuilder query = QueryBuilders.matchAllQuery();
        //关键字搜索条件
        if (StringUtils.isNotBlank(params.getKeyword())) {
            BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery()
                    .should(QueryBuilders.matchQuery("jobName", params.getKeyword()))
                    .should(QueryBuilders.matchQuery("jobInfo", params.getKeyword()))
                    .should(QueryBuilders.matchQuery("jobAddr", params.getKeyword()))
                    .should(QueryBuilders.matchQuery("companyName", params.getKeyword()))
                    .should(QueryBuilders.matchQuery("companyInfo", params.getKeyword()))
                    .should(QueryBuilders.matchQuery("companyAddr", params.getKeyword()));

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
        //工作经验
        if (StringUtils.isNotBlank(params.getExp())) {
            String[] part = params.getExp().split("-");
            int min = Integer.parseInt(part[0]);
            int max = Integer.parseInt(part[1]);
            BoolQueryBuilder expRangeQuery = QueryBuilders.boolQuery()
                    .must(QueryBuilders.rangeQuery("expMin").gt(min).includeLower(true))
                    .must(QueryBuilders.rangeQuery("expMax").lt(max).includeUpper(true));
            query = QueryBuilders.boolQuery().must(expRangeQuery).must(query);

        }
        //发布时间条件
        if (params.getTime() != 0) {
            Date to = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(to);
            rightNow.add(Calendar.DAY_OF_YEAR, (params.getTime() - 1) * -1);
            rightNow.set(Calendar.HOUR_OF_DAY, 0);
            rightNow.set(Calendar.MINUTE, 0);
            rightNow.set(Calendar.SECOND, 0);
            Date from = rightNow.getTime();
            RangeQueryBuilder timeRangeQuery = QueryBuilders.rangeQuery("time")
                    .format("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .gt(from)
                    .lt(to)
                    .includeLower(false)
                    .includeUpper(true);
            query = QueryBuilders.boolQuery().must(timeRangeQuery).must(query);
        }
        //地区搜索
        if (!params.getPlace().equals("全国")) {
            TermsQueryBuilder jobAddrQuery = QueryBuilders.termsQuery("jobAddr",
                    AreaUtil.province.get(params.getPlace()));
            query = QueryBuilders.boolQuery().must(jobAddrQuery).must(query);
        }
        //学历要求
        if (params.getEducation() > 0) {
            TermQueryBuilder educationQuery = QueryBuilders.termQuery("education", params.getEducation());
            query = QueryBuilders.boolQuery().must(educationQuery).must(query);
        }
        //构建查询语句
        queryBuilder.withQuery(query);
        return getTableJobResult(queryBuilder, params.getSortName(), params.getSortOrder(), PageRequest.of(page - 1, size));


    }

    private TableJobResult getTableJobResult(NativeSearchQueryBuilder queryBuilder, String sName, String sortOrder, PageRequest of) {

        //设置排序方式
        if (StringUtils.isNotBlank(sName)) {
            SortOrder order = SortOrder.fromString(sortOrder);
            String sortName = "salary".equals(sName) ? "salaryMin" : "time";
            queryBuilder.withSort(SortBuilders.fieldSort(sortName).order(order));
        }
        //设置分页
        queryBuilder.withPageable(of);
        Page<JobInfo> pages = jobInfoDao.search(queryBuilder.build());
        //设置总结果数量
        TableJobResult tableJobResult = new TableJobResult();
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

    @Override
    public IndexInfo getInfo() {
        IndexInfo info = new IndexInfo();
        //查询节点健康情况
        String url = baseUrl + "/_cluster/health?pretty";
        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
        JSONObject parse = JSON.parseObject(res.getBody());
        info.setStatus(parse.getString("status"));
        //查询节点其它信息
        url = baseUrl + "/jobinfo/_stats/docs,store,segments";
        res = restTemplate.getForEntity(url, String.class);
        parse = JSONObject.parseObject(res.getBody());
        //得到分片数
        int shards = parse.getJSONObject("_shards").getIntValue("total");
        info.setShards(shards);
        JSONObject primaries = parse.getJSONObject("_all").getJSONObject("primaries");
        //得到文档数量
        int docNum = primaries.getJSONObject("docs").getIntValue("count");
        info.setDocNum(docNum);
        //得到存储大小
        BigInteger sizeBytes = primaries.getJSONObject("store").getBigInteger("size_in_bytes");
        info.setSizeBytes(sizeBytes);
        //得到segments信息
        JSONObject segments = primaries.getJSONObject("segments");
        //得到分段数
        int segmentCount = segments.getIntValue("count");
        info.setSegmentCount(segmentCount);
        //内存中文件大小
        BigInteger memoryBytes = segments.getBigInteger("memory_in_bytes");
        info.setMemoryBytes(memoryBytes);
        //词条索引所占大小
        BigInteger termsBytes = segments.getBigInteger("terms_memory_in_bytes");
        info.setTermsBytes(termsBytes);
        //保存字段所占大小
        BigInteger storedBytes = segments.getBigInteger("stored_fields_memory_in_bytes");
        info.setStoredBytes(storedBytes);
        //词条向量内存大小
        BigInteger vectorsBytes = segments.getBigInteger("term_vectors_memory_in_bytes");
        info.setVectorsBytes(vectorsBytes);
        BigInteger normsBytes = segments.getBigInteger("norms_memory_in_bytes");
        info.setNormsBytes(normsBytes);
        BigInteger pointsBytes = segments.getBigInteger("points_memory_in_bytes");
        info.setPointsBytes(pointsBytes);
        BigInteger docBytes = segments.getBigInteger("doc_values_memory_in_bytes");
        info.setDocBytes(docBytes);
        return info;
    }

}
