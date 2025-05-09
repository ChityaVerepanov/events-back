package com.behl.flare.controller;

import com.behl.flare.annotations.PublicEndpoint;
import com.behl.flare.dto.ExceptionResponseDto;
import com.behl.flare.dto.eventcard.EventCardRequest;
import com.behl.flare.dto.eventcard.EventCardResponse;
import com.behl.flare.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Tag(name = "Events Management", description = "Endpoints for managing events.")
public class EventCardController {

	private final EventService eventService;

	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Создание мероприятия")
	@ApiResponse(responseCode = "200", description = "Мероприятие создано",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(responseCode = "401", description = "Сбой авторизации: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Некорректное тело запроса",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<HttpStatus> createEvent(@Valid @RequestBody EventCardRequest request) {
		eventService.createEventCard(request);
		return ResponseEntity.ok().build();
	}


//	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@PublicEndpoint
	@GetMapping(value = "/{eventId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Получение мероприятия")
	@ApiResponse(
			responseCode = "200", description = "Мероприятие получено успешно")
	@ApiResponse(
			responseCode = "404", description = "Мероприятие не найдено",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "401", description = "Сбой авторизации: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "403", description = "Недостаточно прав доступа",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<EventCardResponse> getEvent(
			@PathVariable(required = true, name = "eventId") final Long eventId) {
		final var response = eventService.getEventCard(eventId);
		return ResponseEntity.ok(response);
	}


	@PublicEndpoint
//	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Получение мероприятий с пейджингом")
	@ApiResponse(
			responseCode = "200", description = "Task details retrieved successfully",
			content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
							implementation = EventCardResponse.class
					)
			)
	)
	@ApiResponse(
			responseCode = "401", description = "Сбой авторизации: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<Page<EventCardResponse>> getEvents(
			@PageableDefault(page = 0, size = 10) @ParameterObject Pageable pageable
	) {
		Page<EventCardResponse> response = eventService.getEventCards(pageable);
		return ResponseEntity.ok(response);
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
	@PutMapping(value = "/{eventId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Обновление мероприятия")
	@ApiResponse(
			responseCode = "200", description = "Task details updated successfully",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(
			responseCode = "404", description = "No task exists in the system with provided-id",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "401", description = "Authentication failure: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "403", description = "Access denied: Insufficient permissions",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<HttpStatus> update(
			@PathVariable(required = true, name = "eventId") Long eventId,
			@Valid @RequestBody EventCardRequest request) {
		eventService.updateEventCard(eventId, request);
		return ResponseEntity.ok().build();
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
	@DeleteMapping(value = "/{eventId}")
	@Operation(summary = "Deletes a task record", description = "Delete a specific task by its ID")
	@ApiResponse(
			responseCode = "200", description = "Task deleted successfully",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(
			responseCode = "404", description = "No task exists in the system with provided-id",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "401", description = "Authentication failure: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "403", description = "Access denied: Insufficient permissions",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<HttpStatus> delete(
			@PathVariable(required = true, name = "eventId") Long eventId) {
		eventService.delete(eventId);
		return ResponseEntity.ok().build();
	}



	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@Operation(summary = "Добавление мероприятия в избранное текущему юзеру")
	@ApiResponse(responseCode = "200", description = "Мероприятие добавлено в избранное",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(responseCode = "401", description = "Сбой авторизации: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Некорректное тело запроса",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@PostMapping(value = "/favorite/{eventCardId}")
	public ResponseEntity<HttpStatus> addEventToFavorite(
			@PathVariable(required = true, name = "eventCardId") final Long eventCardId) {
		eventService.addEventToFavorite(eventCardId);
		return ResponseEntity.ok().build();
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@Operation(summary = "Удаление мероприятия из избранного у текущего юзера")
	@ApiResponse(
			responseCode = "200", description = "Успешное удаление",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(
			responseCode = "403", description = "Access denied: Insufficient permissions",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@DeleteMapping(value = "/favorite/{eventCardId}")
	public ResponseEntity<HttpStatus> removeEventFromFavorite(
			@PathVariable(required = true, name = "eventCardId") Long eventCardId) {
		eventService.removeEventFromFavorite(eventCardId);
		return ResponseEntity.ok().build();
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@Operation(summary = "Добавление мероприятия в запланированные текущему юзеру")
	@ApiResponse(responseCode = "200", description = "Мероприятие добавлено в запланированные",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(responseCode = "401", description = "Сбой авторизации: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(responseCode = "400", description = "Некорректное тело запроса",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@PostMapping(value = "/planned/{eventCardId}")
	public ResponseEntity<HttpStatus> addEventToPlanned(
			@PathVariable(required = true, name = "eventCardId") final Long eventCardId) {
		eventService.addEventToPlanned(eventCardId);
		return ResponseEntity.ok().build();
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'CREATOR', 'USER')")
	@Operation(summary = "Удаление мероприятия из запланированных у текущего юзера")
	@ApiResponse(
			responseCode = "200", description = "Успешное удаление",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(
			responseCode = "403", description = "Access denied: Insufficient permissions",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@DeleteMapping(value = "/planned/{eventCardId}")
	public ResponseEntity<HttpStatus> removeEventFromPlanned(
			@PathVariable(required = true, name = "eventCardId") Long eventCardId) {
		eventService.removeEventFromPlanned(eventCardId);
		return ResponseEntity.ok().build();
	}

}