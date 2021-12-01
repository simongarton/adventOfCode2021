package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Challenge1 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private static final String FILENAME = "data/day1.txt";

    public long run() {
        return this.countIncreasesInDepth();
    }

    private long countIncreasesInDepth() {
        final List<Long> longList = this.getLongs();
        final long start = System.currentTimeMillis();
        long last = longList.get(0);
        int index = 1;
        int increases = 0;
        while (index < longList.size()) {
            final long current = longList.get(index);
            if (current > last) {
                increases++;
            }
            last = current;
            index++;
        }
        return increases;
    }

    private List<Long> getLongs() {
        List<Long> longList = new ArrayList<>();
        try (final Stream<String> lines = Files.lines(Paths.get(FILENAME))) {
            longList = lines.mapToLong(Long::new).boxed().collect(Collectors.toList());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return longList;
    }
}
