package com.ajacker.searchengine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ajacker
 * @date 2019/10/27 22:02
 */
@RestController
public class SearchController {
    @RequestMapping("/search")
    public void search(String keyword,String salary,String time,String education,String exp,String place){
        System.out.println(keyword);
        System.out.println(salary);
        System.out.println(time);
        System.out.println(education);
        System.out.println(exp);
        System.out.println(place);
    }
}
