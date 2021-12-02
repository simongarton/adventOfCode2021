package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Challenge1 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Sonar Sweep 1";
    private static final String TITLE_2 = "Sonar Sweep 2";

    public void run(final String[] lines) {
        final List<Long> values = Arrays.stream(lines).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        this.part1(values);
        this.part2(values);
    }

    private long part1(final List<Long> values) {
        final long start = System.currentTimeMillis();
        final int increases = this.countIncreases(values);
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
            final long current = values.get(index++);
            if (current > last) {
                increases++;
            }
            last = current;
        }
        return increases;
    }

    private long part2(final List<Long> values) {
        final long start = System.currentTimeMillis();
        final List<Long> sumList = new ArrayList<>();
        for (int i = 2; i < values.size(); i++) {
            sumList.add(values.get(i - 2) + values.get(i - 1) + values.get(i));
        }
        final int increases = this.countIncreases(sumList);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                increases,
                System.currentTimeMillis() - start));
        return increases;
    }
}
