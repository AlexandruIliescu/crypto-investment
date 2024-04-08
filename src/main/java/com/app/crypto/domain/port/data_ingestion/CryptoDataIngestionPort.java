package com.app.crypto.domain.port.data_ingestion;

import com.app.crypto.domain.model.CryptoPrice;

import java.util.List;

public interface CryptoDataIngestionPort {

    boolean existsFileChecksum(String checksum);

    void saveAllCryptoPrices(List<CryptoPrice> cryptoPrices);

    void saveFileChecksum(String checksum);
}