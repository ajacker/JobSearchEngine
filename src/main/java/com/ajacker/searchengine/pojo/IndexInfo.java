package com.ajacker.searchengine.pojo;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author ajacker
 * @date 2019/11/13 11:21
 */
@Data
public class IndexInfo {
    BigInteger memoryBytes;
    BigInteger termsBytes;
    BigInteger storedBytes;
    BigInteger vectorsBytes;
    BigInteger normsBytes;
    BigInteger pointsBytes;
    BigInteger docBytes;
    private String status;
    private int shards;
    private int docNum;
    private int segmentCount;
    private BigInteger sizeBytes;
}
