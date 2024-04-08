package com.app.crypto.unit_tests;

import com.app.crypto.application.service.data_ingestion.CryptoDataIngestionServiceImpl;
import com.app.crypto.domain.port.data_ingestion.CryptoDataIngestionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoDataIngestionServiceImplTest {

    @Mock
    private CryptoDataIngestionPort cryptoDataIngestionPort;

    @InjectMocks
    private CryptoDataIngestionServiceImpl cryptoDataIngestionService;

    private MockMultipartFile file;

    @BeforeEach
    void setUp() {
        file = new MockMultipartFile("file", "filename.csv", "text/plain", "timestamp,symbol,price\n1641009600000,BTC,46813.21\n".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void whenIngestCryptoData_givenNewFile_thenSuccess() throws IOException {
        // Arrange
        when(cryptoDataIngestionPort.existsFileChecksum(anyString())).thenReturn(false);

        // Act
        cryptoDataIngestionService.ingestCryptoData(file);

        // Assert
        verify(cryptoDataIngestionPort, times(1)).saveAllCryptoPrices(anyList());
        verify(cryptoDataIngestionPort, times(1)).saveFileChecksum(anyString());
    }
}