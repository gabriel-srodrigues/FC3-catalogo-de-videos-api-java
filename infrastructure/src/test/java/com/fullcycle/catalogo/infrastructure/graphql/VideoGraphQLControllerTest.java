package com.fullcycle.catalogo.infrastructure.graphql;

import com.fullcycle.catalogo.GraphQLControllerTest;
import com.fullcycle.catalogo.application.castmember.get.GetCastMemberById;
import com.fullcycle.catalogo.application.category.get.GetCategoryById;
import com.fullcycle.catalogo.application.genre.get.GetGenreById;
import com.fullcycle.catalogo.application.genre.list.ListGenreUseCase;
import com.fullcycle.catalogo.application.video.get.GetVideoUseCase;
import com.fullcycle.catalogo.application.video.list.ListVideoUseCase;
import com.fullcycle.catalogo.application.video.save.SaveVideoUseCase;
import com.fullcycle.catalogo.domain.Fixture;
import com.fullcycle.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = VideoGraphQLController.class)
class VideoGraphQLControllerTest {

    @MockBean
    private ListVideoUseCase listVideoUseCase;

    @MockBean
    private SaveVideoUseCase saveVideoUseCase;

    @MockBean
    private GetVideoUseCase getVideoUseCase;

    @MockBean
    private GetCategoryById getCategoryById;

    @MockBean
    private GetCastMemberById getCastMemberById;

    @MockBean
    private GetGenreById getGenreById;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListGenresShouldReturn() {
        // given
        final var expectedVideo = GetVideoUseCase.Output.from(Fixture.Videos.systemDesign());

        final var expectedVideoCategories = List.of(GetCategoryById.Output.from(Fixture.Categories.aulas()));
        final var expectedVideoCastMembers = List.of(GetCastMemberById.Output.from(Fixture.CastMembers.luiz()));
        final var expectedVideoGenres = List.of(GetGenreById.Output.from(Fixture.Genres.marketing()));

        when(this.getVideoUseCase.execute(any())).thenReturn(Optional.of(expectedVideo));
        when(this.getCategoryById.execute(any())).thenReturn(expectedVideoCategories);
        when(this.getCastMemberById.execute(any())).thenReturn(expectedVideoCastMembers);
        when(this.getGenreById.execute(any())).thenReturn(expectedVideoGenres);

        final var query = """
                query VideoById($id: String!) {
                  video: videoOfId(videoId: $id) {
                    id
                    title
                    categories {
                      id
                      name
                    }
                    castMembers {
                      id
                      name
                    }
                    genres {
                      id
                      name
                    }
                    createdAt
                    updatedAt
                  }
                }
                """;

        // when
        final var res = this.graphql.document(query)
                .variable("id", expectedVideo.id())
                .execute();

        final var actualVideo = res.path("video")
                .entity(Map.class)
                .get();

        // then
        Assertions.assertEquals(expectedVideo.title(), actualVideo.get("title"));
    }
}