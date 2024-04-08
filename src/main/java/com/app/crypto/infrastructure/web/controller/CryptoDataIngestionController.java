package com.app.crypto.infrastructure.web.controller;

import com.app.crypto.application.service.data_ingestion.CryptoDataIngestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
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

    @Operation(summary = "Ingest cryptocurrency data from a CSV file",
            description = "Uploads a CSV file containing cryptocurrency data for ingestion. The file should have a specific format with headers 'timestamp', 'symbol', and 'price'.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "File ingested successfully", content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
                    @ApiResponse(responseCode = "400", description = "Bad request"),
                    @ApiResponse(responseCode = "409", description = "File already uploaded")
            })
    @PostMapping("/prices")
    public ResponseEntity<String> ingestCryptoData(@RequestParam("file") MultipartFile file) {
        cryptoDataIngestionService.ingestCryptoData(file);
        return ResponseEntity.ok("File ingested successfully.");
    }
}