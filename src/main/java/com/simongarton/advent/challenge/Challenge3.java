package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Challenge3 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Binary Diagnostic 1";
    private static final String TITLE_2 = "Binary Diagnostic 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    private int part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final int messageLength = lines[0].length();
        final int messageCount = lines.length;
        final List<Integer> counts = new ArrayList<>();
        for (int index = 0; index < messageLength; index++) {
            final int ones = this.countOnesAtIndex(index, Arrays.asList(lines));
            counts.add(ones);
        }
        StringBuilder gamma = new StringBuilder();
        StringBuilder epsilon = new StringBuilder();
        for (int index = 0; index < messageLength; index++) {
            if (counts.get(index) > (messageCount / 2)) {
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }
        final Integer gammaValue = Integer.parseInt(gamma.toString(), 2);
        final Integer epsilonValue = Integer.parseInt(epsilon.toString(), 2);
        this.logger.info(gammaValue + " * " + epsilonValue);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                gammaValue * epsilonValue,
                System.currentTimeMillis() - start));
        return gammaValue * epsilonValue;
    }

    private int countOnesAtIndex(final int index, final List<String> lines) {
        int ones = 0;
        for (final String message : lines) {
            if (message.charAt(index) == '1') {
                ones++;
            }
        }
        return ones;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        List<String> currentLines = new ArrayList<>(Arrays.asList(lines));
        while (currentLines.size() > 1) {
            currentLines = this.reduceList(currentLines, 0, '1', '0');
        }
        long oxygen = Integer.parseInt(currentLines.get(0), 2);
        currentLines = new ArrayList<>(Arrays.asList(lines));
        while (currentLines.size() > 1) {
            currentLines = this.reduceList(currentLines, 0, '0', '1');
        }
        long co2 = Integer.parseInt(currentLines.get(0), 2);
        this.logger.info(oxygen + " * " + co2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                oxygen * co2,
                System.currentTimeMillis() - start));
        return oxygen * co2;
    }

    private List<String> reduceList(final List<String> currentLines, int index, char v1, char v2) {
        if (currentLines.size() == 1) {
            return currentLines;
        }
        final List<String> newLines = new ArrayList<>();
        final int ones = this.countOnesAtIndex(index, currentLines);
        char match = v2;
        if (ones >= (currentLines.size() / 2.0)) {
            match = v1;
        }
        for (final String line : currentLines) {
            if (line.charAt(index) == match) {
                newLines.add(line);
            }
        }
        return this.reduceList(newLines, ++index, v1, v2);
    }
}
