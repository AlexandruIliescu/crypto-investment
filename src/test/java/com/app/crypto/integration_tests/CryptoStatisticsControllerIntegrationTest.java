package com.app.crypto.integration_tests;

import com.app.crypto.application.service.statistics.CryptoStatisticsService;
import com.app.crypto.domain.model.CryptoNormalizedRange;
import com.app.crypto.domain.model.CryptoRange;
import com.app.crypto.domain.model.CryptoStatistics;
import com.app.crypto.domain.model.CryptoValueStatistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CryptoStatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoStatisticsService cryptoStatisticsService;

    @Test
    void whenGetCryptoMinMax_givenValidRequest_thenSuccess() throws Exception {
        // Arrange
        String symbol = "BTC";
        int year = 2021;
        int month = 1;

        // Initialize mockResponse with properties
        CryptoStatistics mockResponse = CryptoStatistics.builder()
                .oldestTimestamp(Instant.parse("2021-01-01T00:00:00Z")) // Adjusted to match the actual format
                .newestTimestamp(Instant.parse("2021-01-31T23:59:59Z")) // Adjusted to match the actual format
                .minPrice(new BigDecimal("30000"))
                .maxPrice(new BigDecimal("40000"))
                .build();

        when(cryptoStatisticsService.getCryptoMinMax(symbol, year, month)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics")
                        .param("symbol", symbol)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oldestTimestamp").value("2021-01-01T00:00:00Z")) // Adjusted to match the actual format
                .andExpect(jsonPath("$.newestTimestamp").value("2021-01-31T23:59:59Z")) // Adjusted to match the actual format
                .andExpect(jsonPath("$.minPrice").value(30000))
                .andExpect(jsonPath("$.maxPrice").value(40000));
    }

    @Test
    void whenGetCryptoRanges_thenSuccess() throws Exception {
        // Arrange
        List<CryptoRange> mockResponse = List.of(new CryptoRange("BTC", new BigDecimal("40000"), new BigDecimal("30000")));
        when(cryptoStatisticsService.getCryptoRanges()).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/ranges"))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCryptoValueStatistics_givenSymbol_thenSuccess() throws Exception {
        // Arrange
        String symbol = "BTC";
        CryptoValueStatistics mockResponse = new CryptoValueStatistics(Instant.now(), Instant.now(), new BigDecimal("30000"), new BigDecimal("40000"));
        when(cryptoStatisticsService.getCryptoValueStatistics(symbol)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/{symbol}/values", symbol))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetHighestNormalizedRangeCrypto_givenDate_thenSuccess() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2021, 1, 1);
        CryptoNormalizedRange mockResponse = new CryptoNormalizedRange("BTC", new BigDecimal("0.5"));
        when(cryptoStatisticsService.getHighestNormalizedRangeCrypto(date)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/highest-normalized-range")
                        .param("date", date.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void whenGetCryptoMinMax_givenInvalidMonth_thenBadRequest() throws Exception {
        // Arrange
        String symbol = "BTC";
        int year = 2021;
        int invalidMonth = 13;

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics")
                        .param("symbol", symbol)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(invalidMonth)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetCryptoMinMax_givenInvalidYear_thenBadRequest() throws Exception {
        // Arrange
        String symbol = "BTC";
        int year = 1800;
        int invalidMonth = 1;

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics")
                        .param("symbol", symbol)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(invalidMonth)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenGetCryptoValueStatistics_givenEmptySymbol_thenNotFound() throws Exception {
        // Arrange
        String emptySymbol = "";

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/{symbol}/values", emptySymbol))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetCryptoRanges_withNoData_thenEmptyList() throws Exception {
        // Arrange
        when(cryptoStatisticsService.getCryptoRanges()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/ranges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void whenGetCryptoMinMax_givenValidParams_thenReturnsStats() throws Exception {
        // Arrange
        String symbol = "BTC";
        int year = 2021;
        int month = 5;
        CryptoStatistics expectedStats = new CryptoStatistics(
                Instant.parse("2021-05-01T00:00:00Z"),
                Instant.parse("2021-05-31T23:59:59Z"),
                new BigDecimal("30000"),
                new BigDecimal("50000")
        );

        when(cryptoStatisticsService.getCryptoMinMax(symbol, year, month)).thenReturn(expectedStats);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics")
                        .param("symbol", symbol)
                        .param("year", String.valueOf(year))
                        .param("month", String.valueOf(month)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.oldestTimestamp").value("2021-05-01T00:00:00Z"))
                .andExpect(jsonPath("$.newestTimestamp").value("2021-05-31T23:59:59Z"))
                .andExpect(jsonPath("$.minPrice").value(30000))
                .andExpect(jsonPath("$.maxPrice").value(50000));
    }

    @Test
    void whenGetCryptoRanges_thenReturnsListOfRanges() throws Exception {
        // Arrange
        List<CryptoRange> ranges = List.of(
                new CryptoRange("BTC", new BigDecimal("50000"), new BigDecimal("30000")),
                new CryptoRange("ETH", new BigDecimal("4000"), new BigDecimal("2000"))
        );
        when(cryptoStatisticsService.getCryptoRanges()).thenReturn(ranges);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/ranges"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("BTC"))
                .andExpect(jsonPath("$[0].maxPrice").value(50000))
                .andExpect(jsonPath("$[0].minPrice").value(30000))
                .andExpect(jsonPath("$[1].symbol").value("ETH"))
                .andExpect(jsonPath("$[1].maxPrice").value(4000))
                .andExpect(jsonPath("$[1].minPrice").value(2000));
    }

    @Test
    void whenGetCryptoValueStatistics_givenSymbol_thenReturnsStats() throws Exception {
        // Arrange
        String symbol = "BTC";
        CryptoValueStatistics stats = new CryptoValueStatistics(
                Instant.now().minusSeconds(10000),
                Instant.now(),
                new BigDecimal("25000"),
                new BigDecimal("55000")
        );
        when(cryptoStatisticsService.getCryptoValueStatistics(symbol)).thenReturn(stats);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/{symbol}/values", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.minValue").value(25000))
                .andExpect(jsonPath("$.maxValue").value(55000));
    }

    @Test
    void whenGetHighestNormalizedRangeCrypto_givenDate_thenReturnsHighestRangeCrypto() throws Exception {
        // Arrange
        LocalDate date = LocalDate.of(2021, 5, 20);
        CryptoNormalizedRange highestRange = new CryptoNormalizedRange("ETH", new BigDecimal("0.75"));
        when(cryptoStatisticsService.getHighestNormalizedRangeCrypto(date)).thenReturn(highestRange);

        // Act & Assert
        mockMvc.perform(get("/api/crypto-statistics/highest-normalized-range")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("ETH"))
                .andExpect(jsonPath("$.normalizedRange").value(0.75));
    }
}