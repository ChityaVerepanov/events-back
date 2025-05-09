package com.behl.flare.dto;

import com.behl.flare.enums.EventCategory;
import com.behl.flare.enums.EventGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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

	@NotNull(message = "Название мероприятия не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Название мероприятия")
	private String eventName;

	@NotNull(message = "Описание мероприятия не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Описание мероприятия")
	private String eventDescription;

	@NotNull(message = "Дата начала мероприятия не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Дата начала мероприятия")
	private LocalDate dateStart;

	@NotNull(message = "Дата конца мероприятия не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Дата конца мероприятия")
	private LocalDate dateEnd;

	@NotNull(message = "Местоположение мероприятия не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Местоположение мероприятия")
	private String place;

	@NotNull(message = "Организатор мероприятия - не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Организатор мероприятия")
	private String organizerName;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Сайт компании")
	private String organizerSite;

	@NotNull(message = "Категория мероприятия - не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Категория мероприятия")
	private EventCategory category;

	@NotNull(message = "Жанр мероприятия - не должно быть пустым")
	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Жанр мероприятия")
	private EventGenre genre;

}
