package com.behl.flare.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@JsonNaming(value = PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@Schema(title = "EventRequest", accessMode = Schema.AccessMode.WRITE_ONLY)
public class EventCardRequest {

	@NotBlank(message = "Текст мероприятия не должен быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Текст мероприятия",
			example = "Супертуса")
	private String message;

	@NotBlank(message = "Имя файла не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Имя файла - картинки мероприятия",
			example = "Какое-то имя файла...")
	private String fileName;

	@NotNull(message = "Идентификатор пользователя - создателя карточки")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Идентификатор пользователя Firebase")
	private String creatorId;

}
