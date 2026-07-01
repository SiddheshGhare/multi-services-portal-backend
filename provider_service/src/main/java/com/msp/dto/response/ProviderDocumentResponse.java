package com.msp.dto.response;

import com.msp.enums.DocumentType;
import com.msp.enums.VerificationStatus;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter 
@Setter
@Data
@Builder
public class ProviderDocumentResponse {

    private Long id;

    private DocumentType documentType;

    private String documentUrl;

    private VerificationStatus verificationStatus;

    private String remarks;
}
