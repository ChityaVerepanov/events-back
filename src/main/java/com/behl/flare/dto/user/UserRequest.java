package com.behl.flare.dto.user;

import com.behl.flare.enums.Roles;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "UserRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class UserRequest {

/*
	@NotBlank(message = "Идентификатор пользователя Firebase не может быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Идентификатор пользователя Firebase")
	private String firebaseId;
*/


/*
	@NotBlank(message = "Email не может быть пустым")
	@Email(message = "Email должен быть верного формата")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Email пользователя")
	private String email;


	@NotBlank(message = "Мобильный номер пользователя не может быть пустым")
	@Pattern(regexp = "^\\+\\d{11,15}$", message = "Формат мобильного номера пользователя ")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Мобильный номер пользователя", example = "+79991234567")
	private String phoneNumber;
*/


	@NotBlank(message = "Имя и фамилия не могут быть пустыми")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Имя и фамилия", example = "Иван Иванов")
	private String displayName;


	@NotNull(message = "Роль пользователя не может быть пустой")
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "Роль пользователя", example = "ROLE_USER")
	private Roles role;

}