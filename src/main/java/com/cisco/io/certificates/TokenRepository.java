package com.cisco.io.certificates;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<CertificateData, String> {
	List<CertificateData> findAllByTokenReceivedDateAfter(LocalDateTime localDateTime);
}
