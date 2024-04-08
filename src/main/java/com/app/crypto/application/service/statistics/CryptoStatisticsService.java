package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoStatistics;

public interface CryptoStatisticsService {

    CryptoStatistics getCryptoStatistics(String symbol, int year, int month);
}