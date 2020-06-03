package com.cisco.io.certificates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalPdfCertificatesCreator {

	public static final String LOCAL_FOLDER_PATH = "C:\\pdf\\";
	public static final String PDF_EXTENSION = ".pdf";

	private final TokenRepository tokenRepository;
	private final CertificateTemplateProvider certificateTemplateProvider;

	/**
	 * Class used for development purposes to bulk generate certificates as pdf and save it into c:\pdf\* folder
	 */
	@PostConstruct
	public void onInit() {
//		saveBulkPdf(); // Use only when you wand to create pdf's locally to dev purposes. Enjoy :)
	}

	private void saveBulkPdf() {
		try {
			File file = new File(LOCAL_FOLDER_PATH);
			FileUtils.deleteDirectory(file);
			if (!file.exists()) {
				if (file.mkdir()) {
					log.info("Directory created.");
				} else {
					log.error("Failed to create directory.");
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			List<CertificateData> allByTokenReceivedDateAfter = tokenRepository.findAllByTokenReceivedDateAfter(LocalDateTime.now().minusHours(1));
			List<CertificateData> notEmptyFrom = new ArrayList<>();

			allByTokenReceivedDateAfter.forEach(certificateData -> {
				if (certificateData.getTokenPassedBy() != null && !certificateData.getTokenPassedBy().isEmpty()) {
					notEmptyFrom.add(certificateData);
				}
			});

			List<String> allTemplatesToSaveAsPdf = new ArrayList<>();
			notEmptyFrom.forEach(certificateData -> allTemplatesToSaveAsPdf.add(certificateTemplateProvider.provideAsString(certificateData)));

			List<ByteArrayOutputStream> inputStreamList = new ArrayList<>();
			allTemplatesToSaveAsPdf.forEach(templateString -> inputStreamList.add(CertificateGenerator.createPdf(templateString)));

			inputStreamList.forEach(byteArrayOutputStream -> {
				String uuid = UUID.randomUUID().toString();
				String filename = LOCAL_FOLDER_PATH + uuid + PDF_EXTENSION;
				try {
					FileOutputStream output = new FileOutputStream(filename);
					output.write(byteArrayOutputStream.toByteArray());
					output.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			});
		}
	}
}
