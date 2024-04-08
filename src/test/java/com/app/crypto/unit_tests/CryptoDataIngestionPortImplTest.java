package com.app.crypto.unit_tests;

import com.app.crypto.domain.model.CryptoPrice;
import com.app.crypto.domain.port.data_ingestion.CryptoDataIngestionPortImpl;
import com.app.crypto.infrastructure.persistence.checksum.FileChecksumRepository;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoDataIngestionPortImplTest {

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @Mock
    private FileChecksumRepository fileChecksumRepository;

    @InjectMocks
    private CryptoDataIngestionPortImpl cryptoDataIngestionPort;

    @BeforeEach
    void setUp() {
        cryptoDataIngestionPort = new CryptoDataIngestionPortImpl(cryptoPriceRepository, fileChecksumRepository);
    }

    @Test
    void whenFileChecksumExists_thenReturnsTrue() {
        // Arrange
        String checksum = "testChecksum";
        when(fileChecksumRepository.existsByChecksum(checksum)).thenReturn(true);

        // Act
        boolean exists = cryptoDataIngestionPort.existsFileChecksum(checksum);

        // Assert
        assertTrue(exists);
        verify(fileChecksumRepository).existsByChecksum(checksum);
    }

    @Test
    void whenSaveAllCryptoPrices_thenEntitiesAreSaved() {
        // Arrange
        List<CryptoPrice> prices = List.of(
                new CryptoPrice(Instant.now(), "BTC", new BigDecimal("10000")),
                new CryptoPrice(Instant.now(), "ETH", new BigDecimal("2000"))
        );

        // Inspect the argument passed to saveAll
        doAnswer(invocation -> {
            List<?> entities = invocation.getArgument(0);
            assertEquals(2, entities.size());
            return null;
        }).when(cryptoPriceRepository).saveAll(anyList());

        // Act
        cryptoDataIngestionPort.saveAllCryptoPrices(prices);

        // Assert
        verify(cryptoPriceRepository).saveAll(anyList());
    }

    @Test
    void whenSaveFileChecksum_thenChecksumIsSaved() {
        // Arrange
        String checksum = "testChecksum";

        // Act
        cryptoDataIngestionPort.saveFileChecksum(checksum);

        // Assert
        verify(fileChecksumRepository).save(any());
    }
}