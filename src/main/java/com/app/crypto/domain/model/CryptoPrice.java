package com.app.crypto.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CryptoPrice {

    private Instant timestamp;
    private String symbol;
    private BigDecimal price;
    private String checksum;
}