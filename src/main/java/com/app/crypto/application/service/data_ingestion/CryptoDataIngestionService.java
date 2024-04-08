package com.app.crypto.application.service.data_ingestion;

import org.springframework.web.multipart.MultipartFile;

public interface CryptoDataIngestionService {

    /**
     * Processes the ingestion of cryptocurrency data from an uploaded CSV file.
     * The method checks for file uniqueness based on its checksum, parses the CSV to extract
     * cryptocurrency data, and saves the data in the database. If the file has already been uploaded
     * (based on checksum), a {@link DuplicateCsvFileException} is thrown.
     *
     * @param file the CSV file containing cryptocurrency data
     * @throws DuplicateCsvFileException if the file has already been uploaded
     * @throws IngestCryptoDataException if an error occurs during file processing
     */
    void ingestCryptoData(MultipartFile file);
}