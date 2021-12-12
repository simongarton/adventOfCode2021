package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Challenge11 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Dumbo Octopus 1";
    private static final String TITLE_2 = "Dumbo Octopus 2";

    private int[] octopi;
    private int width;
    private int height;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge11() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        this.width = lines[0].length();
        this.height = lines.length;
        this.octopi = new int[this.width * this.height];
        this.loadOctopi(lines);
        long result = 0;
        for (int step = 0; step < 100; step ++) {
            result += this.doOneStep();
            System.out.println(step + 1 + ":" + result);
        }
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private int doOneStep() {
        // first update all
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                this.octopi[(row * this.width) + col]++;
            }
        }
        this.printOctopi(-2);
        return this.nines();
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        this.width = lines[0].length();
        this.height = lines.length;
        this.octopi = new int[this.width * this.height];
        this.loadOctopi(lines);
        long result = 0;
        int step = 0;
        while(true) {
            step ++;
            int changes = this.doOneStep();
            if (changes == 100) {
                result = step;
                break;
            }
        }
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private void loadOctopi(final String[] lines) {
        int index = 0;
        for (final String line : lines) {
            for (int i = 0; i < this.width; i++) {
                this.octopi[index++] = Integer.parseInt(line.charAt(i) + "");
            }
        }
    }

    private int nines() {
        int flashes = 0;
        while (true) {
            final List<String> toBeDone = new ArrayList<>();
            for (int row = 0; row < this.height; row++) {
                for (int col = 0; col < this.width; col++) {
                    if (this.octopi[(row * this.width) + col] == 0) {
                        System.out.println("skipping zero " + row + "," + col);
                        continue;
                    }
                    if (this.octopi[(row * this.width) + col] > 9) {
                        toBeDone.add(row + "," + col);
                    }
                }
            }
            if (toBeDone.isEmpty()) {
                break;
            }
            for (final String excited : toBeDone) {
                final String[] parts = excited.split(",");
                final int row = Integer.parseInt(parts[0]);
                final int col = Integer.parseInt(parts[1]);
                System.out.println("flashing " + row + "," + col);
                flashes++;
                this.octopi[(row * this.width) + col] = 0;
                this.boost(row + 1, col + 1);
                this.boost(row + 1, col + 0);
                this.boost(row + 1, col - 1);
                this.boost(row + 0, col + 1);
                this.boost(row + 0, col - 1);
                this.boost(row - 1, col + 1);
                this.boost(row - 1, col + 0);
                this.boost(row - 1, col - 1);
            }
        }
        this.printOctopi(-1);
        return flashes;
    }

    private void boost(final int row, final int col) {
        if (row < 0 || row >= this.height) {
            return;
        }
        if (col < 0 || col >= this.width) {
            return;
        }
        if (this.octopi[(row * this.width) + col] == 0) {
            return;
        }
        System.out.println("boosting " + row + "," + col + " from " + this.octopi[(row * this.width) + col] + " to " + (this.octopi[(row * this.width) + col] + 1));
        this.octopi[(row * this.width) + col] = this.octopi[(row * this.width) + col] + 1;
    }

    private void printOctopi(final int step) {
        System.out.println("Step " + step + "\n");
        for (int row = 0; row < this.height; row++) {
            final StringBuilder line = new StringBuilder();
            for (int col = 0; col < this.width; col++) {
                final int val = this.octopi[(row * this.width) + col];
                if (val < 10) {
                    line.append(val);
                } else {
                    line.append("x");
                }
            }
            System.out.println(line);
        }
        System.out.println("");
    }
}
