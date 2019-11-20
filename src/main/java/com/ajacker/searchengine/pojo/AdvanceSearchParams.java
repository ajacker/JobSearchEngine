package com.ajacker.searchengine.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author ajacker
 * @date 2019/11/20 12:59
 */
@Data
public class AdvanceSearchParams {
    /**
     * 工作名关键字
     */
    private String jobNameKeyWord;
    /**
     * 工作名搜索类型
     * 1：模糊
     * 2:精确
     */
    private int jobNameType;
    /**
     * 公司名关键字
     */
    private String companyNameKeyWord;
    /**
     * 公司名搜索类型
     * 1：模糊
     * 2:精确
     */
    private int companyNameType;
    /**
     * 目标省份
     */
    private List<String> addressProvence;
    /**
     * 地址额外关键字
     */
    private String addressExtra;
    /**
     * 教育搜索类型
     * 1：至少
     * 2：匹配
     */
    private int eduType;
    /**
     * 教育级别
     */
    private int eduLevel;
    /**
     * 最小工资(k)
     */
    private int minSalary;
    /**
     * 最大工资(k)
     */
    private int maxSalary;
    /**
     * 起始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateFrom;
    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date dateTo;
    /**
     * 当前页码
     */
    private int pageNumber;
    /**
     * 一页几个
     */
    private int pageSize;
    /**
     * 排序列名
     */
    private String sortName;
    /**
     * 排序方式asc/desc
     */
    private String sortOrder;
}
