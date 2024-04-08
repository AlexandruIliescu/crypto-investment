package com.app.crypto.unit_tests;

import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;
import com.app.crypto.domain.port.statistics.CryptoStatisticsPortImpl;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CryptoStatisticsPortImplTest {

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @InjectMocks
    private CryptoStatisticsPortImpl cryptoStatisticsPort;

    @BeforeEach
    void setUp() {
        cryptoStatisticsPort = new CryptoStatisticsPortImpl(cryptoPriceRepository);
    }

    @Test
    void whenGetCryptoMinMax_thenRetrieveStatistics() {
        // Arrange
        String symbol = "BTC";
        int year = 2021;
        int month = 5;
        BigDecimal expectedMinPrice = new BigDecimal("30000");
        BigDecimal expectedMaxPrice = new BigDecimal("40000");
        Instant expectedOldestTimestamp = Instant.now().minusSeconds(1000);
        Instant expectedNewestTimestamp = Instant.now();

        CryptoPriceEntity oldestEntity = new CryptoPriceEntity();
        oldestEntity.setTimestamp(expectedOldestTimestamp);
        CryptoPriceEntity newestEntity = new CryptoPriceEntity();
        newestEntity.setTimestamp(expectedNewestTimestamp);

        when(cryptoPriceRepository.findMinPriceBySymbolAndMonth(eq(symbol), any(Instant.class), any(Instant.class))).thenReturn(Optional.of(expectedMinPrice));
        when(cryptoPriceRepository.findMaxPriceBySymbolAndMonth(eq(symbol), any(Instant.class), any(Instant.class))).thenReturn(Optional.of(expectedMaxPrice));
        when(cryptoPriceRepository.findOldestAndNewestBySymbolAndMonth(eq(symbol), any(Instant.class), any(Instant.class), eq(PageRequest.of(0, 1, Sort.by("timestamp").ascending())))).thenReturn(List.of(oldestEntity));
        when(cryptoPriceRepository.findOldestAndNewestBySymbolAndMonth(eq(symbol), any(Instant.class), any(Instant.class), eq(PageRequest.of(0, 1, Sort.by("timestamp").descending())))).thenReturn(List.of(newestEntity));

        // Act
        CryptoStatistics result = cryptoStatisticsPort.getCryptoMinMax(symbol, year, month);

        // Assert
        assertEquals(expectedMinPrice, result.getMinPrice());
        assertEquals(expectedMaxPrice, result.getMaxPrice());
        assertEquals(expectedOldestTimestamp, result.getOldestTimestamp());
        assertEquals(expectedNewestTimestamp, result.getNewestTimestamp());
    }

    @Test
    void whenGetCryptoRanges_thenRetrieveRanges() {
        // Arrange
        List<Object[]> minMaxPrices = Arrays.asList(
                new Object[]{"BTC", BigDecimal.valueOf(40000), BigDecimal.valueOf(30000)}, // maxPrice before minPrice
                new Object[]{"ETH", BigDecimal.valueOf(2500), BigDecimal.valueOf(2000)} // maxPrice before minPrice
        );
        when(cryptoPriceRepository.findMinMaxPricesForAllCryptos()).thenReturn(minMaxPrices);

        // Act
        List<CryptoRange> result = cryptoStatisticsPort.getCryptoRanges();

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.stream().anyMatch(cr ->
                "BTC".equals(cr.getSymbol()) &&
                        BigDecimal.valueOf(40000).compareTo(cr.getMaxPrice()) == 0 &&
                        BigDecimal.valueOf(30000).compareTo(cr.getMinPrice()) == 0)
        );
        assertTrue(result.stream().anyMatch(cr ->
                "ETH".equals(cr.getSymbol()) &&
                        BigDecimal.valueOf(2500).compareTo(cr.getMaxPrice()) == 0 &&
                        BigDecimal.valueOf(2000).compareTo(cr.getMinPrice()) == 0)
        );
    }

    @Test
    void whenGetCryptoValueStatistics_thenRetrieveStatistics() {
        // Arrange
        String symbol = "BTC";
        when(cryptoPriceRepository.findGlobalMinPriceBySymbol(symbol))
                .thenReturn(Optional.of(BigDecimal.valueOf(30000)));
        when(cryptoPriceRepository.findGlobalMaxPriceBySymbol(symbol))
                .thenReturn(Optional.of(BigDecimal.valueOf(40000)));
        when(cryptoPriceRepository.findOldestTimestampBySymbol(symbol))
                .thenReturn(Optional.of(Instant.now().minusSeconds(10000)));
        when(cryptoPriceRepository.findNewestTimestampBySymbol(symbol))
                .thenReturn(Optional.of(Instant.now()));

        // Act
        CryptoValueStatistics result = cryptoStatisticsPort.getCryptoValueStatistics(symbol);

        // Assert
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(30000), result.getMinValue());
        assertEquals(BigDecimal.valueOf(40000), result.getMaxValue());
        assertNotNull(result.getOldestTimestamp());
        assertNotNull(result.getNewestTimestamp());
    }

    @Test
    void whenGetHighestNormalizedRangeCrypto_thenRetrieveCrypto() {
        // Arrange
        LocalDate date = LocalDate.of(2021, 5, 15);
        List<Object[]> minMaxPrices = List.of(
                new Object[]{"BTC", BigDecimal.valueOf(30000), BigDecimal.valueOf(40000)},
                new Object[]{"ETH", BigDecimal.valueOf(2000), BigDecimal.valueOf(3000)}
        );
        when(cryptoPriceRepository.findMinMaxPricesForDate(date)).thenReturn(minMaxPrices);

        // Act
        CryptoNormalizedRange result = cryptoStatisticsPort.getHighestNormalizedRangeCrypto(date);

        // Assert
        assertEquals("ETH", result.getSymbol());
    }
}