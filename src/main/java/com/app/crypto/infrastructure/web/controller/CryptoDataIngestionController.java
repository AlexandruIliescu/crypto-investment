package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.data_ingestion.CryptoDataIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/api/crypto-data")
public class CryptoDataIngestionController {

    private final CryptoDataIngestionService cryptoDataIngestionService;

    public CryptoDataIngestionController(CryptoDataIngestionService cryptoDataIngestionService) {
        this.cryptoDataIngestionService = cryptoDataIngestionService;
    }

    @PostMapping("/prices")
    public ResponseEntity<String> ingestCryptoData(@RequestParam("file") MultipartFile file) {
        cryptoDataIngestionService.ingestCryptoData(file);
        return ResponseEntity.ok("File ingested successfully.");
    }
}