package ru.gw3nax.kudagoapi.service.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.gw3nax.kudagoapi.controller.dto.CategoryRequest;
import ru.gw3nax.kudagoapi.controller.dto.CategoryResponse;
import ru.gw3nax.kudagoapi.entity.Category;
import ru.gw3nax.kudagoapi.mapper.CategoryMapper;
import ru.gw3nax.kudagoapi.repository.CategoryRepository;
import ru.gw3nax.kudagoapi.service.CategoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategories_shouldReturnListOfCategoryResponses() {
        // Arrange
        List<Category> categories = List.of(new Category(1L, "slug", "name"));
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.mapToCategoryResponse(any(Category.class)))
                .thenReturn(new CategoryResponse(1L, "slug", "name"));

        // Act
        List<CategoryResponse> result = categoryService.getCategories();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("slug", result.getFirst().getSlug());
        assertEquals("name", result.getFirst().getName());
    }

    @Test
    void getCategoryById_shouldReturnCategoryResponse() {
        // Arrange
        Category category = new Category(1L, "slug", "name");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryMapper.mapToCategoryResponse(category))
                .thenReturn(new CategoryResponse(1L, "slug", "name"));

        // Act
        CategoryResponse result = categoryService.getCategoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("slug", result.getSlug());
        assertEquals("name", result.getName());
    }

    @Test
    void postCategory_shouldSaveCategory() {
        // Arrange
        CategoryRequest request = new CategoryRequest(1L, "slug", "name");
        Category category = new Category(1L, "slug", "name");
        when(categoryMapper.mapToCategory(request)).thenReturn(category);

        // Act
        categoryService.postCategory(request);

        // Assert
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void deleteCategory_shouldDeleteCategory() {
        // Arrange
        Category category = new Category(1L, "slug", "name");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        categoryService.deleteCategory(1L);

        // Assert
        verify(categoryRepository, times(1)).deleteById(category.getId());
    }

    @Test
    void deleteCategory_shouldThrowException_whenCategoryNotFound() {
        // Arrange
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> categoryService.deleteCategory(2L));
    }

    @Test
    void updateCategory_shouldUpdateCategory() {
        // Arrange
        Category category = new Category(1L, "old-slug", "old-name");
        CategoryRequest request = new CategoryRequest(1L, "new-slug", "new-name");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        // Act
        categoryService.updateCategory(1L, request);

        // Assert
        verify(categoryRepository, times(1)).update(category.getId(), category);
        assertEquals("new-slug", category.getSlug());
        assertEquals("new-name", category.getName());
    }

    @Test
    void updateCategory_shouldThrowException_whenCategoryNotFound() {
        // Arrange
        CategoryRequest request = new CategoryRequest(1L, "new-slug", "new-name");
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        // Assert
        assertThrows(NoSuchElementException.class, () -> categoryService.updateCategory(2L, request));
    }
}

