package com.cisco.io.certificates;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@Document(collection = "token_distribution_reversed")
@TypeAlias("certificate_data")
class CertificateData {

	@Id
	private String id;

	@Field("principle_name")
	private String principleName;

	@Field("principle_img")
	private String principleImg;

	@Field("reason")
	private String reason;

	@Field("ud_to.cec_id")
	private String cecId;

	@Field("ud_from.full_name")
	private String tokenPassedBy;

	@Field("ud_to.full_name")
	private String appreciationTo;

	@Field("token_recieved_date")
	private LocalDateTime tokenReceivedDate;
}
