package ru.gw3nax.kudagoapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gw3nax.customstarter.aspect.Profiling;
import ru.gw3nax.kudagoapi.controller.dto.CategoryRequest;
import ru.gw3nax.kudagoapi.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoapi.service.CategoryService;

import java.util.List;

@Profiling
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/places/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{id}")
    public CategoryResponse getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping()
    public void postCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.postCategory(categoryRequest);
    }

    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
