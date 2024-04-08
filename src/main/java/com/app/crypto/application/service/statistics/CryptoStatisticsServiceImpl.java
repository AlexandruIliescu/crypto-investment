package com.app.crypto.application.service.statistics;

import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;
import com.app.crypto.domain.port.statistics.CryptoStatisticsPort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<CryptoRange> getCryptoRanges() {
        return cryptoStatisticsPort.getCryptoRanges();
    }

    @Override
    public CryptoValueStatistics getCryptoValueStatistics(String symbol) {
        return cryptoStatisticsPort.getCryptoValueStatistics(symbol);
    }

    @Override
    public CryptoNormalizedRange getHighestNormalizedRangeCrypto(LocalDate date) {
        return cryptoStatisticsPort.getHighestNormalizedRangeCrypto(date);
    }
}