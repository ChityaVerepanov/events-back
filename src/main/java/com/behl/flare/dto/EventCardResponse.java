package com.behl.flare.dto;

import com.behl.flare.enums.EventCategory;
import com.behl.flare.enums.EventGenre;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Название мероприятия")
	private String eventName;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Описание мероприятия")
	private String eventDescription;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Дата начала мероприятия")
	private LocalDate dateStart;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Дата конца мероприятия")
	private LocalDate dateEnd;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Местоположение мероприятия")
	private String place;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Организатор мероприятия")
	private String organizerName;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Сайт компании")
	private String organizerSite;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Категория мероприятия")
	private EventCategory category;

	@Schema(requiredMode = RequiredMode.REQUIRED,
			description = "Жанр мероприятия")
	private EventGenre genre;


}
