package com.app.crypto.application.service.data_ingestion;

import com.app.crypto.domain.model.CryptoPrice;
import com.app.crypto.domain.port.data_ingestion.CryptoDataIngestionPort;
import com.app.crypto.exceptions.DuplicateCsvFileException;
import com.app.crypto.exceptions.IngestCryptoDataException;
import com.app.crypto.infrastructure.web.dto.CryptoDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CryptoDataIngestionServiceImpl implements CryptoDataIngestionService {

    private final CryptoDataIngestionPort cryptoDataIngestionPort;

    public CryptoDataIngestionServiceImpl(CryptoDataIngestionPort cryptoDataIngestionPort) {
        this.cryptoDataIngestionPort = cryptoDataIngestionPort;
    }

    @Override
    public void ingestCryptoData(MultipartFile file) {
        try {
            String checksum = DigestUtils.md5Hex(file.getInputStream()).toUpperCase();

            if (cryptoDataIngestionPort.existsFileChecksum(checksum)) {
                throw new DuplicateCsvFileException("File already uploaded.");
            }

            if (!cryptoDataIngestionPort.existsFileChecksum(checksum)) {
                List<CryptoDataDTO> cryptoDataList = parseCsvFile(file);
                List<CryptoPrice> cryptoPrices = convertToDomainModels(cryptoDataList);
                cryptoDataIngestionPort.saveAllCryptoPrices(cryptoPrices);
                cryptoDataIngestionPort.saveFileChecksum(checksum);
            }
        } catch (IOException e) {
            log.error("Ingest crypto data failed: " + e.getMessage());
            throw new IngestCryptoDataException("Ingest crypto data failed for the current file.");
        }
    }

    private List<CryptoDataDTO> parseCsvFile(MultipartFile file) throws IOException {
        List<CryptoDataDTO> cryptoDataList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CryptoDataDTO cryptoDataDTO = new CryptoDataDTO();
                cryptoDataDTO.setTimestamp(Long.parseLong(csvRecord.get("timestamp")));
                cryptoDataDTO.setSymbol(csvRecord.get("symbol"));
                cryptoDataDTO.setPrice(new BigDecimal(csvRecord.get("price")));
                cryptoDataList.add(cryptoDataDTO);
            }
        }
        return cryptoDataList;
    }

    private List<CryptoPrice> convertToDomainModels(List<CryptoDataDTO> cryptoDataList) {
        return cryptoDataList.stream().map(dto -> {
            CryptoPrice cryptoPrice = new CryptoPrice();
            cryptoPrice.setTimestamp(Instant.ofEpochMilli(dto.getTimestamp()));
            cryptoPrice.setSymbol(dto.getSymbol());
            cryptoPrice.setPrice(dto.getPrice());
            return cryptoPrice;
        }).toList();
    }
}