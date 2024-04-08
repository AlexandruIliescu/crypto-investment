package com.app.crypto.infrastructure.persistence.price;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPriceEntity, Long> {

    @Query(value = "SELECT min(c.price) FROM crypto_prices c WHERE c.symbol = :symbol AND c.timestamp BETWEEN :startOfMonth AND :endOfMonth", nativeQuery = true)
    Optional<BigDecimal> findMinPriceBySymbolAndMonth(String symbol, Instant startOfMonth, Instant endOfMonth);

    @Query(value = "SELECT max(c.price) FROM crypto_prices c WHERE c.symbol = :symbol AND c.timestamp BETWEEN :startOfMonth AND :endOfMonth", nativeQuery = true)
    Optional<BigDecimal> findMaxPriceBySymbolAndMonth(String symbol, Instant startOfMonth, Instant endOfMonth);

    @Query("SELECT c FROM CryptoPriceEntity c WHERE c.symbol = :symbol AND c.timestamp BETWEEN :startOfMonth AND :endOfMonth ORDER BY c.timestamp ASC, c.timestamp DESC")
    List<CryptoPriceEntity> findOldestAndNewestBySymbolAndMonth(String symbol, Instant startOfMonth, Instant endOfMonth, Pageable pageable);

    @Query(value = "SELECT symbol, MAX(price) as maxPrice, MIN(price) as minPrice FROM crypto_prices GROUP BY symbol", nativeQuery = true)
    List<Object[]> findMinMaxPricesForAllCryptos();

    @Query(value = "SELECT MIN(price) FROM crypto_prices WHERE symbol = :symbol", nativeQuery = true)
    Optional<BigDecimal> findGlobalMinPriceBySymbol(String symbol);

    @Query(value = "SELECT MAX(price) FROM crypto_prices WHERE symbol = :symbol", nativeQuery = true)
    Optional<BigDecimal> findGlobalMaxPriceBySymbol(String symbol);

    @Query(value = "SELECT timestamp FROM crypto_prices WHERE symbol = :symbol ORDER BY timestamp ASC LIMIT 1", nativeQuery = true)
    Optional<Instant> findOldestTimestampBySymbol(String symbol);

    @Query(value = "SELECT timestamp FROM crypto_prices WHERE symbol = :symbol ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    Optional<Instant> findNewestTimestampBySymbol(String symbol);

    @Query(value = "SELECT symbol, MIN(price) as minPrice, MAX(price) as maxPrice FROM crypto_prices WHERE DATE(timestamp) = :date GROUP BY symbol", nativeQuery = true)
    List<Object[]> findMinMaxPricesForDate(LocalDate date);
}