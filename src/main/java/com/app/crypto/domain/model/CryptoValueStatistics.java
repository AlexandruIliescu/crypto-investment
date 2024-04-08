package com.app.crypto.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CryptoValueStatistics {

    private final Instant oldestTimestamp;
    private final Instant newestTimestamp;
    private final BigDecimal minValue;
    private final BigDecimal maxValue;

    public CryptoValueStatistics(Instant oldestTimestamp, Instant newestTimestamp, BigDecimal minValue, BigDecimal maxValue) {
        this.oldestTimestamp = oldestTimestamp;
        this.newestTimestamp = newestTimestamp;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
}