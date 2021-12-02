package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Challenge0 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Sonar Sweep 1";
    private static final String TITLE_2 = "Sonar Sweep 2";

    public void run(final String[] lines) {
        final List<Long> values = Arrays.stream(lines).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        this.part1(values);
        this.part2(values);
    }

    private long part1(final List<Long> values) {
        return 0;
    }

    private long part2(final List<Long> values) {
        return 0;
    }
}
