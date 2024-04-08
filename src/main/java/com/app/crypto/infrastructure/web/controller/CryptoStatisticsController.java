package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.statistics.CryptoStatisticsService;
import com.app.crypto.domain.model.CryptoStatistics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/crypto-statistics")
public class CryptoStatisticsController {

    private final CryptoStatisticsService statisticsService;

    public CryptoStatisticsController(CryptoStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<CryptoStatistics> getCryptoStatistics(@Valid @RequestParam String symbol,
                                                                @Min(1900) Integer year,
                                                                @Min(1) @Max(12) Integer month) {
        return ResponseEntity.ok(statisticsService.getCryptoStatistics(symbol, year, month));
    }
}