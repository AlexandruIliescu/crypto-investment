package com.app.crypto.domain.port.data_ingestion;

import com.app.crypto.domain.model.CryptoPrice;
import com.app.crypto.infrastructure.persistence.checksum.FileChecksumEntity;
import com.app.crypto.infrastructure.persistence.checksum.FileChecksumRepository;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class CryptoDataIngestionPortImpl implements CryptoDataIngestionPort {

    private final CryptoPriceRepository cryptoPriceRepository;
    private final FileChecksumRepository fileChecksumRepository;

    public CryptoDataIngestionPortImpl(CryptoPriceRepository cryptoPriceRepository, FileChecksumRepository fileChecksumRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
        this.fileChecksumRepository = fileChecksumRepository;
    }

    @Override
    public boolean existsFileChecksum(String checksum) {
        return fileChecksumRepository.existsByChecksum(checksum);
    }

    @Transactional
    @Override
    public void saveAllCryptoPrices(List<CryptoPrice> cryptoPrices) {
        List<CryptoPriceEntity> entities = cryptoPrices.stream()
                .map(this::convertToEntity)
                .toList();
        cryptoPriceRepository.saveAll(entities);
        log.info("All crypto prices were saved successfully.");
    }

    @Transactional
    @Override
    public void saveFileChecksum(String checksum) {
        FileChecksumEntity fileChecksum = new FileChecksumEntity();
        fileChecksum.setChecksum(checksum);
        fileChecksumRepository.save(fileChecksum);
        log.info("Checksum was saved successfully.");
    }

    /**
     * Converts a {@link CryptoPrice} domain model to a {@link CryptoPriceEntity}.
     *
     * @param cryptoPrice The {@link CryptoPrice} domain model to convert.
     * @return The converted {@link CryptoPriceEntity}.
     */
    private CryptoPriceEntity convertToEntity(CryptoPrice cryptoPrice) {
        CryptoPriceEntity entity = new CryptoPriceEntity();
        entity.setTimestamp(cryptoPrice.getTimestamp());
        entity.setSymbol(cryptoPrice.getSymbol());
        entity.setPrice(cryptoPrice.getPrice());
        return entity;
    }
}