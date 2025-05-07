package com.behl.flare.dto;

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
@Schema(title = "UserCreationRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class UserCreationRequest {

	@NotBlank(message = "Email must not be empty")
	@Email(message = "Email must be of valid format")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "email of user", example = "hardik.behl7444@gmail.com")
	private String email;
	
	@NotBlank(message = "Phone number must not be empty")
	@Pattern(regexp = "^\\+\\d{11,15}$", message = "Incorrect phone number")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "phone number of user", example = "+79991234567")
	private String phoneNumber;

	@NotBlank(message = "Full name must not be empty")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Full name of user", example = "John Doe")
	private String displayName;

	@NotBlank(message = "Password must not be empty")
	@Size(min = 6, message = "Password length must be 6 characters long")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "secure password to enable user login", example = "somethingSecure")
	private String password;

}