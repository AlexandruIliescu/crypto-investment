package com.app.crypto.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoNormalizedRange {

    private String symbol;
    private BigDecimal normalizedRange;

    public CryptoNormalizedRange(String symbol, BigDecimal normalizedRange) {
        this.symbol = symbol;
        this.normalizedRange = normalizedRange;
    }
}