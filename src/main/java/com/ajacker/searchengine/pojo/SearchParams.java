package com.ajacker.searchengine.pojo;

import lombok.Data;

/**
 * @author ajacker
 * @date 2019/10/31 23:32
 */
@Data
public class SearchParams {
    /**
     * 搜索关键字
     */
    private String keyword;
    /**
     * 工作地点
     */
    private String place;
    /**
     * 工作经验的要求
     */
    private String exp;
    /**
     * 薪资要求（k）
     */
    private String salary;
    /**
     * 最近time天
     */
    private int time;
    /**
     * 学历最小要求
     */
    private int education;
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
