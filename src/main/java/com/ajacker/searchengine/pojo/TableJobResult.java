package com.ajacker.searchengine.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author ajacker
 * @date 2019/11/1 10:43
 * 呈现给表格的包装类
 */
@Data
public class TableJobResult {
    private long total;
    private List<JobResult> rows;
}
