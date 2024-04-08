package com.app.crypto.domain.port.statistics;

import com.app.crypto.domain.model.CryptoStatistics;

public interface CryptoStatisticsPort {

    CryptoStatistics getCryptoMinMax(String symbol, int year, int month);
}