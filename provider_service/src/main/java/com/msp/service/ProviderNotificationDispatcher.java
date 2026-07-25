package com.msp.service;

import com.msp.entity.ProviderDocument;
import com.msp.entity.ProviderProfile;

public interface ProviderNotificationDispatcher {

    void sendProviderApproved(ProviderProfile provider);

    void sendProviderRejected(ProviderProfile provider);

    void sendDocumentApproved(ProviderDocument document);

    void sendDocumentRejected(ProviderDocument document);
}
