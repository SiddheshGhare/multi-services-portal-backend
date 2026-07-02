package com.msp.dto.request;

import com.msp.enums.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDocumentRequest {

    @NotNull
    private DocumentType documentType;

    @NotBlank
    private String documentUrl;
}