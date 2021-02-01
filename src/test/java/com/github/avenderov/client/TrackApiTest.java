package com.github.avenderov.client;

import com.github.avenderov.model.Track;
import com.github.avenderov.repository.TrackRepository;
import feign.Feign;
import feign.FeignException;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.net.SocketTimeoutException;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrackApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TrackRepository trackRepository;

    private TrackApi underTest;

    @BeforeEach
    void beforeEach() {
        underTest =
            Feign.builder()
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jLogger(TrackApiTest.class))
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .decode404()
                .client(new OkHttpClient())
                .options(new Request.Options(100, TimeUnit.MILLISECONDS, 500, TimeUnit.MILLISECONDS, true))
                .retryer(Retryer.NEVER_RETRY)
                .target(TrackApi.class, "http://localhost:" + port);
    }

    @Test
    void shouldReturnAllTracks() {
        final Collection<Track> tracks = underTest.tracks();

        assertThat(tracks).hasSize(5);
    }

    @Test
    void shouldReturnOneTrack() {
        final Track expectedTrack = trackRepository.getAll().stream().limit(1).findFirst().get();

        final Track track = underTest.track(expectedTrack.getId());

        assertThat(track).isEqualTo(expectedTrack);
    }

    @Test
    void shouldThrowIfTimeout() {
        final Throwable thrown = catchThrowable(
            () -> underTest.track(UUID.randomUUID(), Collections.singletonMap("delay", Boolean.TRUE)));

        assertThat(thrown)
            .isInstanceOf(FeignException.class)
            .hasCauseInstanceOf(SocketTimeoutException.class);
    }

    @Test
    void shouldReturnNullIfNotFound() {
        final Track track = underTest.track(UUID.randomUUID());

        assertThat(track).isNull();
    }
}
