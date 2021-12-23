package com.simongarton.advent.challenge;

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
        Snailfish added = new Snailfish(lines[0], null);
        int index = 0;
        for (final String line : lines) {
            if (index == 0) {
                index++;
                continue;
            }
            final Snailfish addee = new Snailfish(line, null);
            added = Snailfish.add(added, addee);
        }
        System.out.println(added);
        final long result = added.getMagnitude();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        long biggest = 0;
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines.length; j++) {
                if (i == j) {
                    continue;
                }
                // since I mutate these, I have to load them in each time I want to use them
                Snailfish one = new Snailfish(lines[i], null);
                Snailfish two = new Snailfish(lines[j], null);
                Snailfish mega = Snailfish.add(one, two);
                long magnitude = mega.getMagnitude();
                if (magnitude > biggest) {
                    biggest = magnitude;
                }
                one = new Snailfish(lines[i], null);
                two = new Snailfish(lines[j], null);
                mega = Snailfish.add(two, one);
                magnitude = mega.getMagnitude();
                if (magnitude > biggest) {
                    biggest = magnitude;
                }
            }
        }
        final long result = biggest;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }
}
