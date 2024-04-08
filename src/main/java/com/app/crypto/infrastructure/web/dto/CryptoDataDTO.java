package com.app.crypto.infrastructure.web.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoDataDTO {

    private Long timestamp;
    private String symbol;
    private BigDecimal price;
}