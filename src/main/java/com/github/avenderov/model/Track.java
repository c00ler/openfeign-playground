package com.github.avenderov.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Track {

    UUID id;

    String name;

    String artist;

    public Track(final String name, final String artist) {
        this(UUID.randomUUID(), name, artist);
    }
}
