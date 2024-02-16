package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.application.castmember.get.GetCastMemberById;
import com.fullcycle.catalogo.application.category.get.GetCategoryById;
import com.fullcycle.catalogo.application.genre.get.GetGenreById;
import com.fullcycle.catalogo.application.video.get.GetVideoUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class VideoGraphQLController {

    private final ListVideoUseCase listVideoUseCase;
    private final SaveVideoUseCase saveVideoUseCase;
    private final GetVideoUseCase getVideoUseCase;
    private final GetCategoryById getCategoryById;
    private final GetCastMemberById getCastMemberById;
    private final GetGenreById getGenreById;

    public VideoGraphQLController(
            final ListVideoUseCase listVideoUseCase,
            final SaveVideoUseCase saveVideoUseCase,
            final GetVideoUseCase getVideoUseCase,
            final GetCategoryById getCategoryById,
            final GetCastMemberById getCastMemberById,
            final GetGenreById getGenreById
    ) {
        this.listVideoUseCase = Objects.requireNonNull(listVideoUseCase);
        this.saveVideoUseCase = Objects.requireNonNull(saveVideoUseCase);
        this.getVideoUseCase = Objects.requireNonNull(getVideoUseCase);
        this.getCategoryById = Objects.requireNonNull(getCategoryById);
        this.getCastMemberById = Objects.requireNonNull(getCastMemberById);
        this.getGenreById = Objects.requireNonNull(getGenreById);
    }

    @QueryMapping
    public List<ListVideoUseCase.Output> videos(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument(name = "year_launched") final Integer yearLaunched,
            @Argument final String rating,
            @Argument final Set<String> categories,
            @Argument final Set<String> castMembers,
            @Argument final Set<String> genres
    ) {
        final var input = new ListVideoUseCase.Input(page, perPage, search, sort, direction, rating, yearLaunched, categories, castMembers, genres);
        return this.listVideoUseCase.execute(input).data();
    }

    @QueryMapping
    public GetVideoUseCase.Output videoOfId(@Argument String videoId) {
        return getVideoUseCase.execute(new GetVideoUseCase.Input(videoId)).orElse(null);
    }

    @SchemaMapping(typeName = "Video", field = "categories")
    public List<GetCategoryById.Output> categories(GetVideoUseCase.Output video) {
        return this.getCategoryById.execute(new GetCategoryById.Input(video.categories()));
    }

    @SchemaMapping(typeName = "Video", field = "castMembers")
    public List<GetCastMemberById.Output> castMembers(GetVideoUseCase.Output video) {
        return this.getCastMemberById.execute(new GetCastMemberById.Input(video.castMembers()));
    }

    @SchemaMapping(typeName = "Video", field = "genres")
    public List<GetGenreById.Output> genres(GetVideoUseCase.Output video) {
        return this.getGenreById.execute(new GetGenreById.Input(video.genres()));
    }
}
