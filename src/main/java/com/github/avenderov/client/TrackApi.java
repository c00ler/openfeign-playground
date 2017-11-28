package com.github.avenderov.client;

import com.github.avenderov.model.Track;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface TrackApi {

    @RequestLine("GET /tracks")
    Collection<Track> tracks();

    @RequestLine("GET /tracks/{id}")
    Track track(@Param("id") UUID id, @QueryMap Map<String, Object> params);

    @RequestLine("GET /tracks/{id}")
    Track track(@Param("id") UUID id);

}
