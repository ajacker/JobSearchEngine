package com.ajacker.searchengine.controller;

import com.ajacker.searchengine.pojo.SearchParams;
import com.ajacker.searchengine.pojo.TableJobResult;
import com.ajacker.searchengine.service.impl.JobInfoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ajacker
 * @date 2019/10/27 22:02
 */
@RestController
@Slf4j
public class SearchController {
    @Autowired
    private JobInfoServiceImpl jobInfoService;

    @RequestMapping("/search")
    public TableJobResult search(@RequestBody SearchParams params) {
        log.info("获得查询参数：{}", params);
        return jobInfoService.search(params);
    }
}
