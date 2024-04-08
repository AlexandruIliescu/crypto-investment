package com.app.crypto.domain.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
public class CryptoStatistics {

    private Instant oldestTimestamp;
    private Instant newestTimestamp;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}