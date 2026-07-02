package com.msp.service.impl;

import com.msp.dto.request.CreateServiceCategoryRequest;
import com.msp.entity.ServiceCategory;
import com.msp.repository.ServiceCategoryRepository;
import com.msp.service.ServiceCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

    private final ServiceCategoryRepository categoryRepository;

    @Override
    public ServiceCategory createCategory(CreateServiceCategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Service category already exists");
        }

        ServiceCategory category = ServiceCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .active(request.getActive() != null ? request.getActive() : true)
                .build();

        return categoryRepository.save(category);
    }

    @Override
    public ServiceCategory updateCategory(Long categoryId, CreateServiceCategoryRequest request) {

        ServiceCategory category = getCategoryEntity(categoryId);

        categoryRepository.findByNameIgnoreCase(request.getName())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(categoryId)) {
                        throw new RuntimeException("Service category already exists");
                    }
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        return categoryRepository.save(category);
    }

    @Override
    public ServiceCategory getCategoryById(Long categoryId) {
        return getCategoryEntity(categoryId);
    }

    @Override
    public List<ServiceCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(Long categoryId) {
        ServiceCategory category = getCategoryEntity(categoryId);
        categoryRepository.delete(category);
    }

    private ServiceCategory getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Service category not found"));
    }
}