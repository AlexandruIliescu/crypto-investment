package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;

import java.time.LocalDate;
import java.util.List;

public interface CryptoStatisticsService {

    CryptoStatistics getCryptoMinMax(String symbol, int year, int month);
    List<CryptoRange> getCryptoRanges();
    CryptoValueStatistics getCryptoValueStatistics(String symbol);
    CryptoNormalizedRange getHighestNormalizedRangeCrypto(LocalDate date);
}