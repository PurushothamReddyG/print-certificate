package com.cisco.io.certificates;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.attach.Attacher;
import com.itextpdf.html2pdf.exception.Html2PdfException;
import com.itextpdf.kernel.counter.event.IMetaInfo;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.DocumentProperties;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.styledxmlparser.IXmlParser;
import com.itextpdf.styledxmlparser.node.IDocumentNode;
import com.itextpdf.styledxmlparser.node.impl.jsoup.JsoupHtmlParser;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CertificateGenerator {

	private static final String SRC_MAIN_RESOURCES = "src/main/resources";
	private final CertificateDataProvider certificateDataProvider;
	private final CertificateTemplateProvider certificateTemplateProvider;

	/**
	 * printCertificate method used to complex generate PDF documents from data gathered
	 * from mongo db and filled html template passed to pdf document as body
	 * @param tokenId
	 * @param loggedInUser
	 * @return ByteArrayInputStream which contains PDF Document
	 */
	public ByteArrayInputStream printCertificate(String tokenId, String loggedInUser) {

		CertificateData certificateData = certificateDataProvider.provideForUsername(tokenId, loggedInUser);
		String certificateHtmlTemplate = certificateTemplateProvider.provideAsString(certificateData);

		return new ByteArrayInputStream(createPdf(certificateHtmlTemplate).toByteArray());
	}

	/**
	 * method used to convert parsed html data with data to ByteArrayInputStream which contains PDF Document
	 * @param htmlSrc
	 * @return ByteArrayInputStream which contains PDF Document
	 */
	static ByteArrayOutputStream createPdf(String htmlSrc) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		ConverterProperties converterProperties = new ConverterProperties();
		converterProperties.setBaseUri(SRC_MAIN_RESOURCES);
		convertToPdfWithoutMargins(htmlSrc, output, converterProperties);

		return output;
	}

	private static void convertToPdfWithoutMargins(String html, ByteArrayOutputStream pdfStream, ConverterProperties converterProperties) {
		PdfWriter pdfWriter = new PdfWriter(pdfStream);
		PdfDocument pdfDocument = new PdfDocument(pdfWriter, new DocumentProperties().setEventCountingMetaInfo(new HtmlMetaInfo()));
		pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
		Document document;
		document = toDocument(html, pdfDocument, converterProperties);
		document.close();
	}

	private static class HtmlMetaInfo implements IMetaInfo {
		private static final long serialVersionUID = -295587336698550627L;
	}

	/*
	Custom html to document converter
	 */
	private static Document toDocument(String html, PdfDocument pdfDocument, ConverterProperties converterProperties) {
		if (pdfDocument.getReader() != null) {
			throw new Html2PdfException(Html2PdfException.PdfDocumentShouldBeInWritingMode);
		}
		IXmlParser parser = new JsoupHtmlParser();
		IDocumentNode doc = parser.parse(html);

		Document attachedDocument = Attacher.attach(doc, pdfDocument, converterProperties);
		attachedDocument.setMargins(0.00F, 0.00F, 0.00F, 0.00F);
		attachedDocument.getPdfDocument().setDefaultPageSize(PageSize.A4.rotate());
		return attachedDocument;
	}
}
