package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge19 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Beacon Scanner 1";
    private static final String TITLE_2 = "Beacon Scanner 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_1,
                System.currentTimeMillis() - start));
        return result;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_2,
                System.currentTimeMillis() - start));
        return result;
    }
}
