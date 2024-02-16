package com.fullcycle.catalogo.application.category.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetCategoryById extends UseCase<GetCategoryById.Input, List<GetCategoryById.Output>> {

    private final CategoryGateway categoryGateway;

    public GetCategoryById(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public List<Output> execute(final Input input) {
        return this.categoryGateway.findAllById(input.ids).stream()
                .map(Output::from)
                .toList();
    }

    public record Input(Set<String> ids) {
        public Input(String id) {
            this(Set.of(id));
        }
    }

    public record Output(
            String id,
            String name
    ) {

        public static Output from(final Category category) {
            return new Output(category.id(), category.name());
        }
    }
}
