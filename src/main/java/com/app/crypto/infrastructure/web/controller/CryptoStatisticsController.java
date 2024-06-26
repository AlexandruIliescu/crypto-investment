package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.statistics.CryptoStatisticsService;
import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/crypto-statistics")
public class CryptoStatisticsController {

    private final CryptoStatisticsService cryptoStatisticsService;

    public CryptoStatisticsController(CryptoStatisticsService cryptoStatisticsService) {
        this.cryptoStatisticsService = cryptoStatisticsService;
    }

    @Operation(description = "Returns minimum, maximum, oldest, and newest prices for a specified cryptocurrency symbol within a given month and year.")
    @GetMapping
    public ResponseEntity<CryptoStatistics> getCryptoMinMax(@Valid @RequestParam @NotNull String symbol,
                                                            @Min(1900) @NotNull Integer year,
                                                            @Min(1) @Max(12) @NotNull Integer month) {
        return ResponseEntity.ok(cryptoStatisticsService.getCryptoMinMax(symbol, year, month));
    }

    @Operation(description = "Returns a list of all cryptocurrencies along with their normalized ranges (i.e., (max-min)/min).")
    @GetMapping("/ranges")
    public ResponseEntity<List<CryptoRange>> getCryptoRanges() {
        return ResponseEntity.ok(cryptoStatisticsService.getCryptoRanges());
    }

    @Operation(description = "Returns value statistics including oldest, newest, minimum, and maximum prices for a specified cryptocurrency symbol.")
    @GetMapping("/{symbol}/values")
    public ResponseEntity<CryptoValueStatistics> getCryptoValueStatistics(@PathVariable String symbol) {
        return ResponseEntity.ok(cryptoStatisticsService.getCryptoValueStatistics(symbol));
    }

    @Operation(description = "Returns the cryptocurrency that had the highest normalized range ((max-min)/min) on a specific date.")
    @GetMapping("/highest-normalized-range")
    public ResponseEntity<CryptoNormalizedRange> getHighestNormalizedRangeCrypto(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(cryptoStatisticsService.getHighestNormalizedRangeCrypto(date));
    }
}