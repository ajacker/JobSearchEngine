package com.ajacker.searchengine.controller;

import com.ajacker.searchengine.pojo.SearchParams;
import com.ajacker.searchengine.service.impl.JobInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ajacker
 * @date 2019/10/27 22:02
 */
@RestController
public class SearchController {
    @Autowired
    private JobInfoServiceImpl jobInfoService;

    @RequestMapping("/search")
    public void search(@RequestBody SearchParams params) {
        System.out.println(params);

    }
}
