package com.github.avenderov.repository;

import com.github.avenderov.model.Track;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TrackRepository {

    private final Collection<Track> tracks =
            Collections.unmodifiableList(
                    Arrays.asList(
                            new Track("Lay It Down", "Ratt"),
                            new Track("Big Guns", "Skid Row"),
                            new Track("Rock You", "Helix"),
                            new Track("Wild Child", "W.A.S.P"),
                            new Track("The Wild And The Young", "Quiet Riot")));

    public Collection<Track> getAll() {
        return tracks;
    }

    public Optional<Track> findOne(final UUID id) {
        return tracks.stream()
                .filter(t -> t.getId().equals(id))
                .findAny();
    }

}
