package com.app.crypto.domain.port.data_ingestion;

import com.app.crypto.domain.model.CryptoPrice;

import java.util.List;

public interface CryptoDataIngestionPort {

    /**
     * Checks if a file checksum already exists in the database.
     *
     * @param checksum The checksum to check for existence.
     * @return true if the checksum exists, false otherwise.
     */
    boolean existsFileChecksum(String checksum);

    /**
     * Saves a list of {@link CryptoPrice} objects into the database.
     * Each {@link CryptoPrice} object is converted to a {@link CryptoPriceEntity} before saving.
     *
     * @param cryptoPrices The list of {@link CryptoPrice} objects to save.
     */
    void saveAllCryptoPrices(List<CryptoPrice> cryptoPrices);

    /**
     * Saves a new file checksum into the database to track file uniqueness.
     *
     * @param checksum The checksum of the file to be saved.
     */
    void saveFileChecksum(String checksum);
}