package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.port.CryptoDataIngestionPort;
import org.springframework.stereotype.Service;

@Service
public class CryptoStatisticsServiceImpl implements CryptoStatisticsService {

    private final CryptoDataIngestionPort cryptoDataIngestionPort;

    public CryptoStatisticsServiceImpl(CryptoDataIngestionPort cryptoDataIngestionPort) {
        this.cryptoDataIngestionPort = cryptoDataIngestionPort;
    }

    @Override
    public CryptoStatistics getCryptoStatistics(String symbol, int year, int month) {
        return cryptoDataIngestionPort.findCryptoStatistics(symbol, year, month);
    }
}