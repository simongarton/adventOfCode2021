package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<Long> counts = new ArrayList<>();
        for (int index = 0; index < messageLength; index++) {
            counts.add(this.countOnesAtIndex(index, Arrays.asList(lines)));
        }
        // these are inversed - could I just create epsilon from gamma afterwards ?
        final StringBuilder gamma = new StringBuilder();
        final StringBuilder epsilon = new StringBuilder();
        final int half = messageCount / 2;
        for (int index = 0; index < messageLength; index++) {
            if (counts.get(index) > half) {
                gamma.append("1");
                epsilon.append("0");
            } else {
                gamma.append("0");
                epsilon.append("1");
            }
        }
        final Integer gammaValue = Integer.parseInt(gamma.toString(), 2);
        final Integer epsilonValue = Integer.parseInt(epsilon.toString(), 2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                gammaValue * epsilonValue,
                System.currentTimeMillis() - start));
        return gammaValue * epsilonValue;
    }

    private long countOnesAtIndex(final int index, final List<String> lines) {
        return lines.stream().filter(l -> l.charAt(index) == '1').count();
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        List<String> currentLines = new ArrayList<>(Arrays.asList(lines));
        while (currentLines.size() > 1) {
            currentLines = this.reduceList(currentLines, 0, '1', '0');
        }
        final long oxygen = Integer.parseInt(currentLines.get(0), 2);
        currentLines = new ArrayList<>(Arrays.asList(lines));
        while (currentLines.size() > 1) {
            currentLines = this.reduceList(currentLines, 0, '0', '1');
        }
        final long co2 = Integer.parseInt(currentLines.get(0), 2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                oxygen * co2,
                System.currentTimeMillis() - start));
        return oxygen * co2;
    }

    private List<String> reduceList(final List<String> currentLines, final int index, final char v1, final char v2) {
        if (currentLines.size() == 1) {
            return currentLines;
        }
        final long ones = this.countOnesAtIndex(index, currentLines);
        final char match = ones >= (currentLines.size() / 2.0) ? v1 : v2;
        final List<String> newLines = currentLines.stream().filter(l -> l.charAt(index) == match).collect(Collectors.toList());
        return this.reduceList(newLines, index + 1, v1, v2);
    }
}
