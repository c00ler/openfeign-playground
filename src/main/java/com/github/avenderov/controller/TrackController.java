package com.github.avenderov.controller;

import com.github.avenderov.model.Track;
import com.github.avenderov.repository.TrackRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@AllArgsConstructor
public class TrackController {

    private final TrackRepository trackRepository;

    @GetMapping("/tracks")
    public Collection<Track> tracks() {
        return trackRepository.getAll();
    }

    @GetMapping("/tracks/{id}")
    public ResponseEntity<Track> track(@PathVariable("id") final UUID id,
                                       @RequestParam(value = "delay", required = false) final Boolean delay) {
        if (delay != null && delay) {
            sleep();
        }

        return trackRepository.findOne(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private static void sleep() {
        try {
            TimeUnit.MILLISECONDS.sleep(1_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
