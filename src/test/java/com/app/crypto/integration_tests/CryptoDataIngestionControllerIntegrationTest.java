package com.app.crypto.integration_tests;

import com.app.crypto.domain.port.data_ingestion.CryptoDataIngestionPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CryptoDataIngestionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoDataIngestionPort cryptoDataIngestionPort;

    @Test
    void whenIngestCryptoData_givenValidFile_thenSuccess() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "timestamp,symbol,price\n1641009600000,BTC,46813.21\n".getBytes()
        );

        when(cryptoDataIngestionPort.existsFileChecksum(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(multipart("/api/crypto-data/prices")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File ingested successfully."));
    }

    @Test
    void whenIngestCryptoData_givenDuplicateFile_thenThrowException() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "duplicate.csv",
                "text/csv",
                "timestamp,symbol,price\n1641009600001,BTC,46820.00\n".getBytes()
        );

        when(cryptoDataIngestionPort.existsFileChecksum(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(multipart("/api/crypto-data/prices")
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("File already uploaded.")));
    }
}