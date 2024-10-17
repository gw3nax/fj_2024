package ru.gw3nax.kudagoapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoapi.client.CategoryClient;
import ru.gw3nax.kudagoapi.controller.dto.CategoryRequest;
import ru.gw3nax.kudagoapi.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoapi.entity.Category;
import ru.gw3nax.kudagoapi.mapper.CategoryMapper;
import ru.gw3nax.kudagoapi.mapper.KudaGoCategoryMapper;
import ru.gw3nax.kudagoapi.repository.CategoryRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryClient categoryClient;

    @Profiling
    public void init() {
        log.info("Thread: " + Thread.currentThread().getName());
        log.info("Initializing CategoryService");
        categoryClient.getAllEntities()
                .forEach(entity -> categoryRepository.save(KudaGoCategoryMapper.mapToCategory(entity)));
        log.info("Finished Initializing CategoryService");
    }

    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::mapToCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(categoryMapper::mapToCategoryResponse).orElse(null);
    }

    public void postCategory(CategoryRequest request) {
        Category category = categoryMapper.mapToCategory(request);
        categoryRepository.save(category);
    }

    public void updateCategory(Long id, CategoryRequest category) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            Category categoryToUpdate = categoryOptional.get();
            categoryToUpdate.setName(category.getName());
            categoryToUpdate.setSlug(category.getSlug());
            categoryRepository.update(id, categoryToUpdate);
        } else throw new NoSuchElementException();
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.findById(id).isPresent()) {
            categoryRepository.deleteById(id);
        } else throw new NoSuchElementException();
    }
}
