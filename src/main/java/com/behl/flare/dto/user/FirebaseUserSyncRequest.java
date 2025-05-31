package com.behl.flare.dto.user;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "FirebaseUserSyncRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class FirebaseUserSyncRequest {

	@NotBlank(message = "JWT must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "JWT token")
	private String token;
	
}