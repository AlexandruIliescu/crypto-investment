package com.app.crypto.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoRange {

    private String symbol;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;

    public CryptoRange(String symbol, BigDecimal maxPrice, BigDecimal minPrice) {
        this.symbol = symbol;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
    }

    public BigDecimal getNormalizedRange() {
        return maxPrice.subtract(minPrice).divide(minPrice, BigDecimal.ROUND_HALF_UP);
    }
}