package com.app.crypto.domain.port.statistics;

import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;

import java.time.LocalDate;
import java.util.List;

public interface CryptoStatisticsPort {

    /**
     * Retrieves statistical information about a cryptocurrency for a specific month and year, including minimum and maximum prices and timestamps.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @param year The year of interest.
     * @param month The month of interest.
     * @return A {@link CryptoStatistics} object containing statistical information.
     */
    CryptoStatistics getCryptoMinMax(String symbol, int year, int month);

    /**
     * Retrieves a list of {@link CryptoRange} objects for all cryptocurrencies, each including the symbol, minimum price, and maximum price.
     *
     * @return A list of {@link CryptoRange} objects.
     */
    List<CryptoRange> getCryptoRanges();

    /**
     * Retrieves value statistics for a specific cryptocurrency, including oldest and newest timestamps and minimum and maximum prices.
     *
     * @param symbol The symbol of the cryptocurrency.
     * @return A {@link CryptoValueStatistics} object containing the value statistics.
     */
    CryptoValueStatistics getCryptoValueStatistics(String symbol);

    /**
     * Retrieves the cryptocurrency with the highest normalized range (i.e., (max - min) / min) for a specific date.
     *
     * @param date The date of interest.
     * @return A {@link CryptoNormalizedRange} object for the cryptocurrency with the highest normalized range on the specified date.
     */
    CryptoNormalizedRange getHighestNormalizedRangeCrypto(LocalDate date);
}