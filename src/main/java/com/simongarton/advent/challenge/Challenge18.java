package com.simongarton.advent.challenge;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge18 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Snailfish 1";
    private static final String TITLE_2 = "Snailfish 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        for (final String line : lines) {
            final Snailfish snailfish = new Snailfish(line);
            System.out.println(line + " : " + snailfish);
            final Snailfish mutated = addAndReduceSnailfish();
        }
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private Snailfish addAndReduceSnailfish() {
        return null;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }


}
