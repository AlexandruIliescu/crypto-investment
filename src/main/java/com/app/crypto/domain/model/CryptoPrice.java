package com.app.crypto.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@NoArgsConstructor
@Data
public class CryptoPrice {

    private Instant timestamp;
    private String symbol;
    private BigDecimal price;
    private String checksum;

    public CryptoPrice(Instant now, String btc, BigDecimal bigDecimal) {
    }
}