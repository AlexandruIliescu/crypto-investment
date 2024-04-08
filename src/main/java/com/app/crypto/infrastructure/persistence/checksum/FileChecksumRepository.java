package com.app.crypto.infrastructure.persistence.checksum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileChecksumRepository extends JpaRepository<FileChecksumEntity, Long> {

    boolean existsByChecksum(String checksum);
}