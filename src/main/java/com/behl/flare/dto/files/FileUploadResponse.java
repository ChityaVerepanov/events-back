package com.behl.flare.dto.files;

import lombok.Getter;
import lombok.Setter;

//@Getter
//@Setter
public class FileUploadResponse {

    private String fileName;

    public FileUploadResponse(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
