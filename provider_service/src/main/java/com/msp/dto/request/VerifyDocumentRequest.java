
package com.msp.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyDocumentRequest {

    @NotBlank
    private String remarks;
}