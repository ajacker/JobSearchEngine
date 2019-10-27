package com.ajacker.searchengine.task;

import com.ajacker.searchengine.pojo.JobInfo;
import com.ajacker.searchengine.service.IJobInfoService;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author ajacker
 * @date 2019/10/28 1:50
 */
@Component
public class ElasticSearchPipeLine implements Pipeline {
    private final IJobInfoService jobInfoService;

    public ElasticSearchPipeLine(IJobInfoService jobInfoService) {
        this.jobInfoService = jobInfoService;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        JobInfo jobInfo = resultItems.get("jobInfo");
        if (jobInfo != null) {
            jobInfoService.save(jobInfo);
        }
    }
}
