package com.msp.service;

import com.msp.dto.request.CreateServiceCategoryRequest;
import com.msp.entity.ServiceCategory;

import java.util.List;

public interface ServiceCategoryService {

    ServiceCategory createCategory(CreateServiceCategoryRequest request);

    ServiceCategory updateCategory(Long categoryId, CreateServiceCategoryRequest request);

    ServiceCategory getCategoryById(Long categoryId);

    List<ServiceCategory> getAllCategories();

    void deleteCategory(Long categoryId);
}