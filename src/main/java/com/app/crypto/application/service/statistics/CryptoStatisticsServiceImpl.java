package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.port.statistics.CryptoStatisticsPort;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<CryptoRange> findCryptoRanges() {
        return cryptoStatisticsPort.findCryptoRanges();
    }
}