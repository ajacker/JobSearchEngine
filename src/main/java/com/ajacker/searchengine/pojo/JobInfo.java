package com.ajacker.searchengine.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author ajacker
 * @date 2019/10/27 22:17
 */
@Document(indexName = "jobinfo")
@Data
public class JobInfo {
    @Id
    @Field(store = true, type = FieldType.Text)
    private String url;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyName;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyAddr;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String companyInfo;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobName;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobAddr;
    @Field(store = true, analyzer = "ik_smart", searchAnalyzer = "ik_smart", type = FieldType.Text)
    private String jobInfo;
    @Field(store = true, type = FieldType.Integer)
    private Integer salaryMin;
    @Field(store = true, type = FieldType.Integer)
    private Integer salaryMax;
    @Field(store = true, type = FieldType.Integer)
    private Integer expMin;
    @Field(store = true, type = FieldType.Integer)
    private Integer expMax;
    @Field(type = FieldType.Date, store = true,
            format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;
}
