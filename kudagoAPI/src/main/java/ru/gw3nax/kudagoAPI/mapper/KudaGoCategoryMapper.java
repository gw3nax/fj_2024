package ru.gw3nax.kudagoAPI.mapper;

import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoAPI.client.dto.KudaGoCategoryResponse;
import ru.gw3nax.kudagoAPI.entity.Category;

@Component
public class KudaGoCategoryMapper {

    public static Category mapToCategory(KudaGoCategoryResponse kudaGoCategoryResponse) {
        Category category = new Category();
        category.setName(kudaGoCategoryResponse.getName());
        category.setSlug(kudaGoCategoryResponse.getSlug());
        return category;
    }
}
