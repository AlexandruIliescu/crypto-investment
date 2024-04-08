package com.app.crypto.infrastructure.persistence.checksum;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "file_checksums")
public class FileChecksumEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String checksum;
}