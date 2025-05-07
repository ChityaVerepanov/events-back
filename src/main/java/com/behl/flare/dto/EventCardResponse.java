package com.behl.flare.dto;

import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.behl.flare.entity.TaskStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Schema(title = "EventCardResponse", accessMode = Schema.AccessMode.READ_ONLY)
//@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
public class EventCardResponse {

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Идентификатор",
			example = "100")
	private Long id;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Текст мероприятия",
			example = "Супертуса")
	private String message;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Имя файла - картинки мероприятия",
			example = "Какое-то имя файла...")
	private String fileName;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Идентификатор пользователя Firebase")
	private String creatorId;
}
