package com.app.crypto.domain.port.statistics;

import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;

import java.util.List;

public interface CryptoStatisticsPort {

    CryptoStatistics getCryptoMinMax(String symbol, int year, int month);
    List<CryptoRange> findCryptoRanges();
}