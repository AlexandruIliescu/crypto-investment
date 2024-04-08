package com.app.crypto.domain.port.statistics;

import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class CryptoStatisticsPortImpl implements CryptoStatisticsPort{

    private final CryptoPriceRepository cryptoPriceRepository;

    public CryptoStatisticsPortImpl(CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    @Override
    public CryptoStatistics getCryptoMinMax(String symbol, int year, int month) {
        ZoneId utcZoneId = ZoneId.of("UTC");
        LocalDate startOfMonthLocalDate = LocalDate.of(year, month, 1);
        ZonedDateTime startOfMonthZdt = startOfMonthLocalDate.atStartOfDay(utcZoneId);
        Instant startOfMonthInstant = startOfMonthZdt.toInstant();
        LocalDate endOfMonthLocalDate = startOfMonthLocalDate.with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime endOfMonthZdt = endOfMonthLocalDate.atTime(23, 59, 59, 999999999).atZone(utcZoneId);
        Instant endOfMonthInstant = endOfMonthZdt.toInstant();

        Optional<BigDecimal> minPriceOpt = cryptoPriceRepository.findMinPriceBySymbolAndMonth(symbol, startOfMonthInstant, endOfMonthInstant);
        Optional<BigDecimal> maxPriceOpt = cryptoPriceRepository.findMaxPriceBySymbolAndMonth(symbol, startOfMonthInstant, endOfMonthInstant);

        // Fetching oldest and newest prices with a single query
        List<CryptoPriceEntity> oldestAndNewestPrices = cryptoPriceRepository.findOldestAndNewestBySymbolAndMonth(
                symbol, startOfMonthInstant, endOfMonthInstant, PageRequest.of(0, 2, Sort.by("timestamp").ascending())
        );

        if (minPriceOpt.isEmpty() || maxPriceOpt.isEmpty() || oldestAndNewestPrices.size() < 2) {
            throw new EntityNotFoundException("No price data found for " + symbol + " in " + year + "-" + month);
        }

        BigDecimal minPrice = minPriceOpt.orElseThrow(() -> new EntityNotFoundException("Minimum price not found"));
        BigDecimal maxPrice = maxPriceOpt.orElseThrow(() -> new EntityNotFoundException("Maximum price not found"));

        CryptoStatistics statistics = new CryptoStatistics();
        statistics.setMinPrice(minPrice);
        statistics.setMaxPrice(maxPrice);
        statistics.setOldestTimestamp(oldestAndNewestPrices.get(0).getTimestamp());
        statistics.setNewestTimestamp(oldestAndNewestPrices.get(1).getTimestamp());

        return statistics;
    }
}