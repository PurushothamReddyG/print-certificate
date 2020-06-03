package com.cisco.io.certificates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import static java.text.MessageFormat.format;

@Component
@Slf4j
@RequiredArgsConstructor
class CertificateTemplateProvider {

	private static final String CLASSPATH_CERTIFICATE_TEMPLATE_CERTIFICATE_TEMPLATE_HTML = "classpath:certificate-template/certificate-template.html";

	private final HtmlCertificatePlaceholderParser htmlCertificatePlaceholderParser;

	/**
	 * method to provide html template from resource folder
	 * @param certificateData
	 * @return html template with placeholders or empty string if html is not found
	 */
	String provideAsString(CertificateData certificateData) {
		try {
			String templateToFillInData = FileUtils.readFileToString(ResourceUtils.getFile(CLASSPATH_CERTIFICATE_TEMPLATE_CERTIFICATE_TEMPLATE_HTML), Charset.defaultCharset());
			return htmlCertificatePlaceholderParser.fillWithData(templateToFillInData, certificateData);
		} catch (IOException e) {
			log.error(format("Unable to provide certificate template file located from path: {0}", CLASSPATH_CERTIFICATE_TEMPLATE_CERTIFICATE_TEMPLATE_HTML));
			return StringUtils.EMPTY;
		}
	}
}
