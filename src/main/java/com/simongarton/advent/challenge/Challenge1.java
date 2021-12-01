package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Challenge1 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String FILENAME = "data/day1.txt";
    private static final String TITLE_1 = "Sonar Sweep 1";
    private static final String TITLE_2 = "Sonar Sweep 2";

    public void run() {
        this.part1();
        this.part2();
    }

    private long part1() {
        final List<Long> longList = this.readValues();
        final long start = System.currentTimeMillis();
        final int increases = this.countIncreases(longList);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                increases,
                System.currentTimeMillis() - start));
        return increases;
    }

    private int countIncreases(final List<Long> values) {
        long last = values.get(0);
        int index = 1;
        int increases = 0;
        while (index < values.size()) {
            final long current = values.get(index);
            if (current > last) {
                increases++;
            }
            last = current;
            index++;
        }
        return increases;
    }

    private long part2() {
        final List<Long> longList = this.readValues();
        final long start = System.currentTimeMillis();
        final List<Long> sumList = new ArrayList<>();
        for (int i = 2; i < longList.size(); i++) {
            sumList.add(longList.get(i - 2) + longList.get(i - 1) + longList.get(i));
        }
        final int increases = this.countIncreases(sumList);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                increases,
                System.currentTimeMillis() - start));
        return increases;
    }

    private List<Long> readValues() {
        try (final Stream<String> lines = Files.lines(Paths.get(FILENAME))) {
            return lines.mapToLong(Long::valueOf).boxed().collect(Collectors.toList());
        } catch (final IOException e) {
            this.logger.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
