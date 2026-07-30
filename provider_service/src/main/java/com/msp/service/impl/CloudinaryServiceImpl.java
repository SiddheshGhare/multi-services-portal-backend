package com.msp.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.msp.service.CloudinaryService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {

        validateFile(file);

        try {
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "msp/provider-files",
                                    "resource_type", "auto"
                            )
                    );

            Object secureUrl = uploadResult.get("secure_url");

            if (secureUrl == null) {
                throw new RuntimeException(
                        "Unable to upload file"
                );
            }

            return secureUrl.toString();

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to upload file",
                    exception
            );
        }
    }

    @Override
    public void deleteFile(String fileUrl) {

        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String publicId = extractPublicId(fileUrl);

            if (publicId == null || publicId.isBlank()) {
                return;
            }

            cloudinary.uploader().destroy(
                    publicId,
                    ObjectUtils.asMap(
                            "resource_type",
                            getResourceType(fileUrl)
                    )
            );

        } catch (Exception exception) {
            /*
             * File deletion failure should not block
             * profile or document update.
             */
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException(
                    "File cannot be empty"
            );
        }

        String contentType = file.getContentType();

        boolean allowed =
                "image/jpeg".equalsIgnoreCase(contentType)
                        || "image/png".equalsIgnoreCase(contentType)
                        || "image/webp".equalsIgnoreCase(contentType)
                        || "application/pdf".equalsIgnoreCase(contentType);

        if (!allowed) {
            throw new RuntimeException(
                    "Only JPG, PNG, WEBP and PDF files are allowed"
            );
        }

        long maximumSize = 10L * 1024 * 1024;

        if (file.getSize() > maximumSize) {
            throw new RuntimeException(
                    "File size cannot exceed 10 MB"
            );
        }
    }

    private String extractPublicId(String fileUrl) {

        int uploadIndex = fileUrl.indexOf("/upload/");

        if (uploadIndex == -1) {
            return null;
        }

        String value = fileUrl.substring(
                uploadIndex + "/upload/".length()
        );

        value = value.replaceFirst("^v\\d+/", "");

        int lastDotIndex = value.lastIndexOf('.');

        if (lastDotIndex != -1) {
            value = value.substring(0, lastDotIndex);
        }

        return value;
    }

    private String getResourceType(String fileUrl) {

        String lowerCaseUrl = fileUrl.toLowerCase();

        if (lowerCaseUrl.endsWith(".pdf")) {
            return "raw";
        }

        return "image";
    }
}