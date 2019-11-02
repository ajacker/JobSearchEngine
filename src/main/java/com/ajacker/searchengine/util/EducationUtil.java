package com.ajacker.searchengine.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ajacker
 * @date 2019/11/2 14:48
 */
public class EducationUtil {
    public static Set<String> educations = new HashSet<>();
    public static Map<String, Integer> educationMap = new HashMap<>();

    static {
        educations.add("初中及以下");
        educations.add("高中");
        educations.add("中技");
        educations.add("中专");
        educations.add("大专");
        educations.add("本科");
        educations.add("硕士");
        educations.add("博士");
        educationMap.put("初中及以下", 1);
        educationMap.put("高中/中技/中专", 2);
        educationMap.put("大专", 3);
        educationMap.put("本科", 4);
        educationMap.put("硕士", 5);
        educationMap.put("博士", 6);
    }

}
