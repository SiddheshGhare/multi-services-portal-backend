package com.msp.dto.request;

import com.msp.enums.DocumentType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
public class UploadDocumentRequest {

    @NotNull
    private DocumentType documentType;

    @NotNull
    private String documentUrl;
}