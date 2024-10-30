package ru.gw3nax.kudagoapi.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gw3nax.kudagoapi.service.CategoryService;

import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class CategoryInitCommand implements InitCommand {
    private final CategoryService categoryService;

    @Override
    public void execute() {
        categoryService.init();
    }
}
