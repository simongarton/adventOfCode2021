package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge17 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Trick Shot 1";
    private static final String TITLE_2 = "Trick Shot 2";

    private static final int COMPLETELY_ARBITRARY_MAX_NUMBER = 1000;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final TargetArea targetArea = new TargetArea(lines[0]);
        final long result = this.highestY(targetArea);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long highestY(final TargetArea targetArea) {
        long highestY = 0;
        final int max = COMPLETELY_ARBITRARY_MAX_NUMBER;
        for (int deltaX = 0; deltaX < max; deltaX++) {
            for (int deltaY = -max; deltaY < max; deltaY++) {
                final Probe probe = new Probe(deltaX, deltaY);
                this.fireProbe(probe, targetArea);
                if (targetArea.probeInside(probe.x, probe.y)) {
                    if (highestY < probe.highestY) {
                        highestY = probe.highestY;
                    }
                }
            }
        }
        return highestY;
    }

    private long totalOptions(final TargetArea targetArea) {
        long totalOptions = 0;
        final int max = COMPLETELY_ARBITRARY_MAX_NUMBER;
        for (int deltaX = 0; deltaX < max; deltaX++) {
            for (int deltaY = -max; deltaY < max; deltaY++) {
                final Probe probe = new Probe(deltaX, deltaY);
                this.fireProbe(probe, targetArea);
                if (targetArea.probeInside(probe.x, probe.y)) {
                    totalOptions++;
                }
            }
        }
        return totalOptions;
    }

    private void fireProbe(final Probe probe, final TargetArea targetArea) {
        while (true) {
            probe.move();
            if (targetArea.probeInside(probe.x, probe.y)) {
                break;
            }
            if (targetArea.probeBelow(probe.x, probe.y)) {
                break;
            }
            if (targetArea.probeBeyond(probe.x, probe.y)) {
                break;
            }
        }
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final TargetArea targetArea = new TargetArea(lines[0]);
        final long result = this.totalOptions(targetArea);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    public static final class Probe {
        int x;
        int y;
        int deltaX;
        int deltaY;
        int highestY;
        int step;

        public Probe(final int deltaX, final int deltaY) {
            this.x = 0;
            this.y = 0;
            this.deltaX = deltaX;
            this.deltaY = deltaY;
            this.highestY = 0;
            this.step = 0;
        }

        public void move() {
            this.x += this.deltaX;
            this.y += this.deltaY;
            if (this.deltaX > 0) {
                this.deltaX--;
            } else {
                if (this.deltaX < 0) {
                    this.deltaX++;
                }
            }
            this.deltaY -= 1;
            this.step++;
            if (this.highestY < this.y) {
                this.highestY = this.y;
            }
        }
    }

    public static final class TargetArea {
        int minX;
        int maxX;
        int minY;
        int maxY;

        public TargetArea(final String line) {
            // target area: x=20..30, y=-10..-5
            final String interest = line.substring(13);
            final String[] parts = interest.split(", ");
            final String x = parts[0].substring(2);
            final String[] xs = x.split("\\.\\.");
            final String y = parts[1].substring(2);
            final String[] ys = y.split("\\.\\.");
            this.minX = Integer.parseInt(xs[0]);
            this.maxX = Integer.parseInt(xs[1]);
            this.minY = Integer.parseInt(ys[0]);
            this.maxY = Integer.parseInt(ys[1]);
        }

        @Override
        public String toString() {
            return this.minX + "," + this.minY + " -> " + this.maxX + "," + this.maxY;
        }

        public boolean probeBelow(final int x, final int y) {
            return y < this.minY;
        }

        public boolean probeBeyond(final int x, final int y) {
            return x > this.maxX;
        }

        public boolean probeInside(final int x, final int y) {
            return (!
                    (
                            (x < this.minX) ||
                                    (x > this.maxX) ||
                                    (y < this.minY) ||
                                    (y > this.maxY)
                    )
            );
        }
    }
}
