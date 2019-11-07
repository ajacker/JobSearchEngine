package com.ajacker.jobspider.pojo;

import com.ajacker.jobspider.spider.monitor.MyStatusMXBean;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author ajacker
 * @date 2019/11/7 21:42
 */
@Component
public class SpiderInfo {
    private final MyStatusMXBean statusMXBean;
    @Getter
    private int downloadSuccess;
    @Getter
    private int downloadError;
    @Getter
    private int analyseSuccess;
    @Getter
    private int analyseError;
    @Getter
    private int leftPage;
    @Getter
    private int totalPage;
    @Getter
    private String downloadSuccessRate;
    @Getter
    private String analyseSuccessRate;
    @Getter
    private String status;

    public SpiderInfo(MyStatusMXBean statusMXBean) {
        this.statusMXBean = statusMXBean;
    }

    public void updateInfo() {
        downloadSuccess = statusMXBean.getDownloadSuccessPageCount();
        downloadError = statusMXBean.getDownloadErrorPageCount();
        totalPage = statusMXBean.getTotalPageCount();
        leftPage = statusMXBean.getLeftPageCount();
        downloadSuccessRate = String.format("%.2f%%", downloadSuccess * 100f / totalPage);
        analyseSuccess = statusMXBean.getAnalyseSuccessPageCount();
        analyseError = statusMXBean.getAnalyseErrorPageCount();
        analyseSuccessRate = String.format("%.2f%%", analyseSuccess * 100f / downloadSuccess);
        status = statusMXBean.getStatus();
    }
}
