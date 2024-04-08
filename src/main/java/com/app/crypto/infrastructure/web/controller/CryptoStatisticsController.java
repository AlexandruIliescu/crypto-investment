package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.statistics.CryptoStatisticsService;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/crypto-statistics")
public class CryptoStatisticsController {

    private final CryptoStatisticsService cryptoStatisticsService;

    public CryptoStatisticsController(CryptoStatisticsService cryptoStatisticsService) {
        this.cryptoStatisticsService = cryptoStatisticsService;
    }

    @GetMapping
    public ResponseEntity<CryptoStatistics> getCryptoMinMax(@Valid @RequestParam @NotNull String symbol,
                                                            @Min(1900) @NotNull Integer year,
                                                            @Min(1) @Max(12) @NotNull Integer month) {
        return ResponseEntity.ok(cryptoStatisticsService.getCryptoMinMax(symbol, year, month));
    }

    @GetMapping("/ranges")
    public ResponseEntity<List<CryptoRange>> getCryptoRanges() {
        List<CryptoRange> ranges = cryptoStatisticsService.findCryptoRanges();
        return ResponseEntity.ok(ranges);
    }
}