package com.fullcycle.catalogo.application.genre.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.category.Category;
import com.fullcycle.catalogo.domain.category.CategoryGateway;
import com.fullcycle.catalogo.domain.genre.Genre;
import com.fullcycle.catalogo.domain.genre.GenreGateway;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetGenreById extends UseCase<GetGenreById.Input, List<GetGenreById.Output>> {

    private final GenreGateway genreGateway;

    public GetGenreById(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public List<Output> execute(final Input input) {
        return this.genreGateway.findAllById(input.ids).stream()
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

        public static Output from(final Genre genre) {
            return new Output(genre.id(), genre.name());
        }
    }
}
