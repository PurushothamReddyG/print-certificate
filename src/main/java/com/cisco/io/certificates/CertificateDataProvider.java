package com.cisco.io.certificates;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CertificateDataProvider {

	private final TokenRepository tokenRepository;

	/**
	 * Provides data used to fill in html template for certificate printing
	 * 
	 * @param tokenId
	 * @param username
	 * @return CertificateData entity
	 */
	CertificateData provideForUsername(String tokenId, String username) {
		Optional<CertificateData> certificateDataFromDb = tokenRepository.findById(tokenId);
		if (!certificateDataFromDb.isPresent()) {
			return null;
		}
		if (!isLoggedUser(username, certificateDataFromDb.get().getCecId())) {
			return null;
		}

		return certificateDataFromDb.get();
	}

	private boolean isLoggedUser(String username, String cecId) {
		return username.equalsIgnoreCase(cecId);
	}
}
