package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.statistics.CryptoStatisticsService;
import com.app.crypto.domain.model.CryptoStatistics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<CryptoStatistics> getCryptoMinMax(@Valid @RequestParam @NotNull String symbol,
                                                            @Min(1900) @NotNull Integer year,
                                                            @Min(1) @Max(12) @NotNull Integer month) {
        return ResponseEntity.ok(statisticsService.getCryptoMinMax(symbol, year, month));
    }
}