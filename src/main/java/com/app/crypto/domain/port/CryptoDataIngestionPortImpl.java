package com.app.crypto.domain.port;

import com.app.crypto.domain.model.CryptoPrice;
import com.app.crypto.infrastructure.persistence.checksum.FileChecksumEntity;
import com.app.crypto.infrastructure.persistence.checksum.FileChecksumRepository;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void saveAllCryptoPrices(List<CryptoPrice> cryptoPrices) {
        List<CryptoPriceEntity> entities = cryptoPrices.stream()
                .map(this::convertToEntity)
                .toList();
        cryptoPriceRepository.saveAll(entities);
    }

    @Override
    public void saveFileChecksum(String checksum) {
        FileChecksumEntity fileChecksum = new FileChecksumEntity();
        fileChecksum.setChecksum(checksum);
        fileChecksumRepository.save(fileChecksum);
    }

    private CryptoPriceEntity convertToEntity(CryptoPrice cryptoPrice) {
        CryptoPriceEntity entity = new CryptoPriceEntity();
        entity.setTimestamp(cryptoPrice.getTimestamp());
        entity.setSymbol(cryptoPrice.getSymbol());
        entity.setPrice(cryptoPrice.getPrice());
        return entity;
    }
}