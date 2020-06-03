package com.cisco.io.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.cisco.io.certificates.CertificateGenerator;

import lombok.RequiredArgsConstructor;

@RequestScope
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PrintCertificateController {

	private final CertificateGenerator certificateGenerator;

	@RequestMapping(value = "/printCertificate", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> printCertificate(HttpServletRequest request,
			@PathParam("token_id") final String token_id) throws IOException {
		ByteArrayInputStream bis = null;
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename=certificate.pdf");
		String loggedInUser = "gjutu";

		bis = certificateGenerator.printCertificate(token_id, loggedInUser);
		if (!StringUtils.isEmpty(bis)) {
			return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
		} else {
			String message = loggedInUser + " does not have any tokens with " + token_id;
			InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
			return ResponseEntity.ok(new InputStreamResource(stream));
		}

	}

}
