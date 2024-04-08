package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;

import java.util.List;

public interface CryptoStatisticsService {

    CryptoStatistics getCryptoMinMax(String symbol, int year, int month);
    List<CryptoRange> findCryptoRanges();
}