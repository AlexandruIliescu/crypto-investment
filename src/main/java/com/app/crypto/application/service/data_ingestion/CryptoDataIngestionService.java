package com.app.crypto.application.service.data_ingestion;

import org.springframework.web.multipart.MultipartFile;

public interface CryptoDataIngestionService {

    void ingestCryptoData(MultipartFile file);
}