package com.behl.flare.controller;

import com.behl.flare.annotations.PublicEndpoint;
import com.behl.flare.dto.ExceptionResponseDto;
import com.behl.flare.dto.files.FileUploadResponse;
import com.behl.flare.service.ImageStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Images Management", description = "Endpoints for managing images.")
@RestController
@RequestMapping("/api/v1/images")
public class ImageUploadController {

    private final ImageStorageService imageStorageService;

    public ImageUploadController(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'CREATOR')")
    @Operation(
            summary = "Загрузить изображение",
            description = "Позволяет загрузить файл изображения. Возвращает имя файла."
    )
    @ApiResponse(responseCode = "200", description = "Файл успешно загружен",
            content = @Content(schema = @Schema(implementation = FileUploadResponse.class)))
    @ApiResponse(responseCode = "400", description = "Некорректный файл",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class)))
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadImage(
            @Parameter(description = "Файл изображения", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = imageStorageService.saveImage(file);
        return ResponseEntity.ok(new FileUploadResponse(fileName));
    }


    @PublicEndpoint
    @Operation(
            summary = "Получить изображение по имени файла",
            description = "Возвращает изображение по его имени, если оно найдено на сервере. В случае успеха возвращается файл-ресурс с корректным Content-Type."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Файл найден и возвращён"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Файл не найден",
            content = @Content(schema = @Schema(implementation = ExceptionResponseDto.class))
    )
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) {
        Resource resource = imageStorageService.loadImageAsResource(fileName);

        // Определение типа контента (можно улучшить)
        String contentType = "application/octet-stream";
        if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            contentType = "image/gif";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(resource);
    }
}
