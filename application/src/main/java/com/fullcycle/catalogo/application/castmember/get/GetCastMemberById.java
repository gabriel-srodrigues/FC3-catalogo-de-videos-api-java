package com.fullcycle.catalogo.application.castmember.get;

import com.fullcycle.catalogo.application.UseCase;
import com.fullcycle.catalogo.domain.castmember.CastMember;
import com.fullcycle.catalogo.domain.castmember.CastMemberGateway;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class GetCastMemberById extends UseCase<GetCastMemberById.Input, List<GetCastMemberById.Output>> {

    private final CastMemberGateway castMemberGateway;

    public GetCastMemberById(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public List<Output> execute(final Input input) {
        return this.castMemberGateway.findAllById(input.ids).stream()
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

        public static Output from(final CastMember castMember) {
            return new Output(castMember.id(), castMember.name());
        }
    }
}
