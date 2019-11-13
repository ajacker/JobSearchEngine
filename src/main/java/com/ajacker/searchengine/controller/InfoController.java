package com.ajacker.searchengine.controller;

import com.ajacker.searchengine.pojo.IndexInfo;
import com.ajacker.searchengine.service.impl.JobInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ajacker
 * @date 2019/11/13 10:18
 */
@RestController
public class InfoController {
    @Autowired
    private JobInfoServiceImpl jobInfoService;

    @RequestMapping(name = "/info", method = RequestMethod.POST)
    public IndexInfo getInfo() {
        return jobInfoService.getInfo();
    }
}
