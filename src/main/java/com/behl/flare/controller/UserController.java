package com.behl.flare.controller;

import com.behl.flare.dto.eventcard.EventCardRequest;
import com.behl.flare.dto.user.UserRequest;
import com.behl.flare.dto.user.UserResponse;
import com.behl.flare.enums.Roles;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.behl.flare.annotations.PublicEndpoint;
import com.behl.flare.dto.ExceptionResponseDto;
import com.behl.flare.dto.TokenSuccessResponseDto;
import com.behl.flare.dto.user.UserCreationRequest;
import com.behl.flare.dto.user.UserLoginRequestDto;
import com.behl.flare.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
	@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Endpoints for user account and authentication management")
public class UserController {

	private final UserService userService;

	@PublicEndpoint
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a user record", description = "Creates a unique user record in the system corresponding to the provided information")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "User record created successfully",
					content = @Content(schema = @Schema(implementation = Void.class))),
			@ApiResponse(responseCode = "409", description = "User account with provided email-id already exists",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request body",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<HttpStatus> createUser(@Valid @RequestBody final UserCreationRequest userCreationRequest) {
		userService.createFirebaseUser(userCreationRequest, Roles.ROLE_USER);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PublicEndpoint
	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Validates user login credentials", description = "Validates user login credentials and returns access-token on successful authentication")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Authentication successfull"),
			@ApiResponse(responseCode = "401", description = "Invalid credentials provided. Failed to authenticate user",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request body",
					content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))) })
	public ResponseEntity<TokenSuccessResponseDto> login(
			@Valid @RequestBody final UserLoginRequestDto userLoginRequest) {
		final var response = userService.login(userLoginRequest);
		return ResponseEntity.ok(response);
	}


	@PreAuthorize("hasRole('ADMIN')")
    @Operation(
			summary = "Получение списка пользователей с пейджингом",
			description = "Требуемые роли: ADMIN")
    @ApiResponse(responseCode = "200", description = "Success request",
			content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
							implementation = UserResponse.class
					)
			))
    @GetMapping
    public Page<UserResponse> getUsers(
            @PageableDefault(page = 0, size = 10) @ParameterObject Pageable pageable
    ) {
        return userService.getUsersPageable(pageable);
    }


	@PreAuthorize("hasAnyRole('ADMIN', 'USER', 'CREATOR')")
    @Operation(
			summary = "Получение данных текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Success request",
			content = @Content(
					mediaType = MediaType.APPLICATION_JSON_VALUE,
					schema = @Schema(
							implementation = UserResponse.class
					)
			))
	@GetMapping(value = "/user_details", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUserDetails() {
        return userService.getUserDetails();
    }


	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Обновление пользователя")
	@ApiResponse(
			responseCode = "200", description = "Updated successfully",
			content = @Content(schema = @Schema(implementation = Void.class)))
	@ApiResponse(
			responseCode = "404", description = "No user with provided Id",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "401", description = "Authentication failure: Invalid access token",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	@ApiResponse(
			responseCode = "403", description = "Access denied: Insufficient permissions",
			content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
	public ResponseEntity<HttpStatus> update(
			@PathVariable(required = true, name = "userId") Long userId,
			@Valid @RequestBody UserRequest request) {
		userService.updateUserById(userId, request);
		return ResponseEntity.ok().build();
	}


}