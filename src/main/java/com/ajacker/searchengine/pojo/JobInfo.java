package com.ajacker.searchengine.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author ajacker
 * @date 2019/10/27 22:17
 */
@Document(indexName = "jobinfo")
@Data
public class JobInfo {
    @Id
    @Field(index = true, store = true, type = FieldType.Long)
    private Long id;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyName;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyAddr;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyInfo;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobName;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobAddr;
    @Field(index = true, store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobInfo;
    @Field(index = true, store = true, type = FieldType.Integer)
    private Integer salaryMin;
    @Field(index = true, store = true, type = FieldType.Integer)
    private Integer salaryMax;
    @Field(index = true, store = true, type = FieldType.Integer)
    private Integer expMin;
    @Field(index = true, store = true, type = FieldType.Integer)
    private Integer expMax;
    @Field(index = true, store = true, type = FieldType.Text)
    private String url;
    //TODO: 换成Date类型
    @Field(index = true, store = true, type = FieldType.Text)
    private String time;
}
