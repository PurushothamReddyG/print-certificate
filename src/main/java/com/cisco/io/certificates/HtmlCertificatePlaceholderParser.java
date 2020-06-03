package com.cisco.io.certificates;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.text.MessageFormat.format;

@Component
class HtmlCertificatePlaceholderParser {

	/**
	 * fillWithData used to replace placeholders in html with provided data
	 * @param templateToFillInData
	 * @param certificateData
	 * @return html with replaced placeholders with proper data as string
	 */
	String fillWithData(String templateToFillInData, CertificateData certificateData) {
		return mapReplaceAll(templateToFillInData, getPlaceholders(certificateData));
	}

	private Map<String, String> getPlaceholders(CertificateData certificateData) {
		Map<String, String> placeholdersMap  = new HashMap<>();
		placeholdersMap.put("\\{\\{tokenPassedBy\\}\\}", certificateData.getTokenPassedBy() != null ? certificateData.getTokenPassedBy() : "");
		placeholdersMap.put("\\{\\{appreciationTo\\}\\}", certificateData.getAppreciationTo() != null ? certificateData.getAppreciationTo() : "");
		placeholdersMap.put("\\{\\{principleName\\}\\}", certificateData.getPrincipleName() != null ? certificateData.getPrincipleName() : "");
		placeholdersMap.put("\\{\\{principleImg\\}\\}", certificateData.getPrincipleImg() != null ? certificateData.getPrincipleImg() : "");
		placeholdersMap.put("\\{\\{tokenReceivedDate\\}\\}", convertToCertificateFormat(certificateData.getTokenReceivedDate()));
		placeholdersMap.put("\\{\\{reason\\}\\}", certificateData.getReason() != null ? certificateData.getReason() : "");

		return placeholdersMap;
	}

	private String mapReplaceAll(String templateString, Map<String, String> replacements) {
		for (Map.Entry<String, String> entry : replacements.entrySet()) {
			templateString = templateString.replaceAll(entry.getKey(), entry.getValue());
		}
		return templateString;
	}

	private String convertToCertificateFormat(LocalDateTime localDateTime) {
		// convert to 20th August 2019
		String day = getFormattedDay(localDateTime.getDayOfMonth());
		String month = localDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
		String year = String.valueOf(localDateTime.getYear());
		return format("{0} {1} {2}", day, month, year);
	}

	private String getFormattedDay(int dayOfMonth) {
		return dayOfMonth + getDayNumberSuffix(dayOfMonth);
	}

	private String getDayNumberSuffix(int day) {
		if (day >= 11 && day <= 13) {
			return "<sup>th</sup>";
		}
		switch (day % 10) {
			case 1:
				return "<sup>st</sup>";
			case 2:
				return "<sup>nd</sup>";
			case 3:
				return "<sup>rd</sup>";
			default:
				return "<sup>th</sup>";
		}
	}
}
