package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.port.statistics.CryptoStatisticsPort;
import org.springframework.stereotype.Service;

@Service
public class CryptoStatisticsServiceImpl implements CryptoStatisticsService {

    private final CryptoStatisticsPort cryptoStatisticsPort;

    public CryptoStatisticsServiceImpl(CryptoStatisticsPort cryptoStatisticsPort) {
        this.cryptoStatisticsPort = cryptoStatisticsPort;
    }

    @Override
    public CryptoStatistics getCryptoMinMax(String symbol, int year, int month) {
        return cryptoStatisticsPort.getCryptoMinMax(symbol, year, month);
    }
}