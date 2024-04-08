package com.app.crypto.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CryptoStatistics {

    private Instant oldestTimestamp;
    private Instant newestTimestamp;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}