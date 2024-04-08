package com.app.crypto.domain.port.statistics;

import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceEntity;
import com.app.crypto.infrastructure.persistence.price.CryptoPriceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class CryptoStatisticsPortImpl implements CryptoStatisticsPort {

    private final CryptoPriceRepository cryptoPriceRepository;

    public CryptoStatisticsPortImpl(CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    @Override
    public CryptoStatistics getCryptoMinMax(String symbol, int year, int month) {
        Instant startOfMonthInstant = getStartOfMonthInstant(year, month);
        Instant endOfMonthInstant = getEndOfMonthInstant(year, month);

        BigDecimal minPrice = getMinPrice(symbol, startOfMonthInstant, endOfMonthInstant);
        BigDecimal maxPrice = getMaxPrice(symbol, startOfMonthInstant, endOfMonthInstant);
        Instant oldestTimestamp = getOldestTimestamp(symbol, startOfMonthInstant, endOfMonthInstant);
        Instant newestTimestamp = getNewestTimestamp(symbol, startOfMonthInstant, endOfMonthInstant);

        return CryptoStatistics.builder()
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .oldestTimestamp(oldestTimestamp)
                .newestTimestamp(newestTimestamp)
                .build();
    }

    @Override
    public List<CryptoRange> getCryptoRanges() {
        List<Object[]> minMaxPrices = cryptoPriceRepository.findMinMaxPricesForAllCryptos();
        return minMaxPrices.stream()
                .map(obj -> new CryptoRange((String) obj[0], (BigDecimal) obj[1], (BigDecimal) obj[2]))
                .sorted((o1, o2) -> o2.getNormalizedRange().compareTo(o1.getNormalizedRange()))
                .toList();
    }

    @Override
    public CryptoValueStatistics getCryptoValueStatistics(String symbol) {
        BigDecimal minPrice = cryptoPriceRepository.findGlobalMinPriceBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException("Min price not found for symbol: " + symbol));
        BigDecimal maxPrice = cryptoPriceRepository.findGlobalMaxPriceBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException("Max price not found for symbol: " + symbol));
        Instant oldestTimestamp = cryptoPriceRepository.findOldestTimestampBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException("Oldest timestamp not found for symbol: " + symbol));
        Instant newestTimestamp = cryptoPriceRepository.findNewestTimestampBySymbol(symbol).orElseThrow(() -> new EntityNotFoundException("Newest timestamp not found for symbol: " + symbol));

        return new CryptoValueStatistics(oldestTimestamp, newestTimestamp, minPrice, maxPrice);
    }

    @Override
    public CryptoNormalizedRange getHighestNormalizedRangeCrypto(LocalDate date) {
        List<Object[]> minMaxPrices = cryptoPriceRepository.findMinMaxPricesForDate(date);
        return minMaxPrices.stream()
                .map(obj -> new CryptoNormalizedRange(
                        (String) obj[0],
                        ((BigDecimal) obj[2]).subtract((BigDecimal) obj[1]).divide((BigDecimal) obj[1], BigDecimal.ROUND_HALF_UP)))
                .max((o1, o2) -> o1.getNormalizedRange().compareTo(o2.getNormalizedRange()))
                .orElseThrow(() -> new EntityNotFoundException("No data found for date: " + date));
    }

    /**
     * Calculates the start of a month in UTC based on a given year and month.
     *
     * @param year The year of interest.
     * @param month The month of interest.
     * @return The {@link Instant} representing the start of the specified month.
     */
    private Instant getStartOfMonthInstant(int year, int month) {
        LocalDate startOfMonthLocalDate = LocalDate.of(year, month, 1);
        ZonedDateTime startOfMonthZdt = startOfMonthLocalDate.atStartOfDay(ZoneId.of("UTC"));
        return startOfMonthZdt.toInstant();
    }

    /**
     * Calculates the end of a month in UTC based on a given year and month.
     *
     * @param year The year of interest.
     * @param month The month of interest.
     * @return The {@link Instant} representing the end of the specified month.
     */
    private Instant getEndOfMonthInstant(int year, int month) {
        LocalDate endOfMonthLocalDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
        ZonedDateTime endOfMonthZdt = endOfMonthLocalDate.atTime(23, 59, 59, 999999999).atZone(ZoneId.of("UTC"));
        return endOfMonthZdt.toInstant();
    }

    /**
     * Retrieves the minimum price of a cryptocurrency between two timestamps.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @param start The start timestamp.
     * @param end The end timestamp.
     * @return The minimum price as a {@link BigDecimal}.
     */
    private BigDecimal getMinPrice(String symbol, Instant start, Instant end) {
        return cryptoPriceRepository.findMinPriceBySymbolAndMonth(symbol, start, end)
                .orElseThrow(() -> new EntityNotFoundException("Minimum price not found for " + symbol));
    }

    /**
     * Retrieves the maximum price of a cryptocurrency between two timestamps.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @param start The start timestamp.
     * @param end The end timestamp.
     * @return The maximum price as a {@link BigDecimal}.
     */
    private BigDecimal getMaxPrice(String symbol, Instant start, Instant end) {
        return cryptoPriceRepository.findMaxPriceBySymbolAndMonth(symbol, start, end)
                .orElseThrow(() -> new EntityNotFoundException("Maximum price not found for " + symbol));
    }

    /**
     * Retrieves the oldest timestamp for a cryptocurrency between two timestamps.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @param start The start timestamp.
     * @param end The end timestamp.
     * @return The oldest timestamp as an {@link Instant}.
     */
    private Instant getOldestTimestamp(String symbol, Instant start, Instant end) {
        return cryptoPriceRepository.findOldestAndNewestBySymbolAndMonth(symbol, start, end, PageRequest.of(0, 1, Sort.by("timestamp").ascending()))
                .stream()
                .findFirst()
                .map(CryptoPriceEntity::getTimestamp)
                .orElseThrow(() -> new EntityNotFoundException("Oldest price not found for " + symbol));
    }

    /**
     * Retrieves the newest timestamp for a cryptocurrency between two timestamps.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @param start The start timestamp.
     * @param end The end timestamp.
     * @return The newest timestamp as an {@link Instant}.
     */
    private Instant getNewestTimestamp(String symbol, Instant start, Instant end) {
        return cryptoPriceRepository.findOldestAndNewestBySymbolAndMonth(symbol, start, end, PageRequest.of(0, 1, Sort.by("timestamp").descending()))
                .stream()
                .findFirst()
                .map(CryptoPriceEntity::getTimestamp)
                .orElseThrow(() -> new EntityNotFoundException("Newest price not found for " + symbol));
    }
}