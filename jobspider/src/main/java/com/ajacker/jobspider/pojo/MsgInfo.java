package com.ajacker.jobspider.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ajacker
 * @date 2019/11/10 23:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgInfo {
    private int status;
    private String message;
}
