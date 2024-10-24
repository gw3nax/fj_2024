package ru.gw3nax.kudagoapi.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoapi.client.dto.KudaGoCategoryResponse;
import ru.gw3nax.kudagoapi.entity.Category;

@Component
public class KudaGoCategoryMapper {

    public static Category mapToCategory(KudaGoCategoryResponse kudaGoCategoryResponse) {
        Category category = new Category();
        category.setName(kudaGoCategoryResponse.getName());
        category.setSlug(kudaGoCategoryResponse.getSlug());
        return category;
    }
}
