package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge5 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Hydrothermal Venture 1";
    private static final String TITLE_2 = "Hydrothermal Venture 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge5() {
    }

    private int part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Floor floor = new Floor(lines, false);
        final int score = floor.overlap(2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                score,
                System.currentTimeMillis() - start));
        return score;
    }

    private int part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Floor floor = new Floor(lines, true);
        final int score = floor.overlap(2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                score,
                System.currentTimeMillis() - start));
        return score;
    }

    private static final class Floor {

        private static final int MAX_DIM = 1000;
        int[] squares = new int[MAX_DIM * MAX_DIM];

        public Floor(final String[] lines, boolean diagonals) {
            for (final String line : lines) {
                this.drawVent(line, diagonals);
            }
        }

        private void drawVent(final String line, boolean diagonals) {
            final String[] coords = line.split("->");
            final Coord coord1 = new Coord(coords[0].trim());
            final Coord coord2 = new Coord(coords[1].trim());
            boolean done = false;
            if (coord1.x == coord2.x) {
                this.drawVerticalVent(coord1, coord2);
                done = true;
            }
            if (coord1.y == coord2.y) {
                this.drawHorizontalVent(coord1, coord2);
                done = true;
            }
            if (!done && diagonals) {
                this.drawDiagonalVent(coord1, coord2);
            }
        }

        private void drawVerticalVent(final Coord coord1, final Coord coord2) {
            final int delta = (coord1.y > coord2.y) ? -1 : +1;
            int startY = coord1.y;
            while (startY != coord2.y) {
                this.squares[(startY * MAX_DIM) + coord1.x] = this.squares[(startY * MAX_DIM) + coord1.x] + 1;
                startY = startY + delta;
            }
            this.squares[(startY * MAX_DIM) + coord1.x] = this.squares[(startY * MAX_DIM) + coord1.x] + 1;
        }

        private void drawHorizontalVent(final Coord coord1, final Coord coord2) {
            final int delta = (coord1.x > coord2.x) ? -1 : +1;
            int startX = coord1.x;
            while (startX != coord2.x) {
                this.squares[(coord1.y * MAX_DIM) + startX] = this.squares[(coord1.y * MAX_DIM) + startX] + 1;
                startX = startX + delta;
            }
            this.squares[(coord1.y * MAX_DIM) + startX] = this.squares[(coord1.y * MAX_DIM) + startX] + 1;
        }

        private void drawDiagonalVent(final Coord coord1, final Coord coord2) {
            final int deltaX = (coord1.x > coord2.x) ? -1 : +1;
            final int deltaY = (coord1.y > coord2.y) ? -1 : +1;
            int startX = coord1.x;
            int startY = coord1.y;
            while (startX != coord2.x) {
                this.squares[(startY * MAX_DIM) + startX] = this.squares[(startY * MAX_DIM) + startX] + 1;
                startX = startX + deltaX;
                startY = startY + deltaY;

            }
            this.squares[(startY * MAX_DIM) + startX] = this.squares[(startY * MAX_DIM) + startX] + 1;
        }

        public void printFloor() {
            for (int row = 0; row < MAX_DIM; row++) {
                final StringBuilder line = new StringBuilder();
                for (int col = 0; col < MAX_DIM; col++) {
                    if (this.squares[(row * MAX_DIM) + col] == 0) {
                        line.append(".");
                    } else {
                        line.append(this.squares[(row * MAX_DIM) + col]);
                    }
                }
                System.out.println(line);
            }
        }

        public int overlap(final int i) {
            int overlaps = 0;
            for (int row = 0; row < MAX_DIM; row++) {
                for (int col = 0; col < MAX_DIM; col++) {
                    if (this.squares[(row * MAX_DIM) + col] >= 2) {
                        overlaps++;
                    }
                }
            }
            return overlaps;
        }
    }

    private static final class Coord {
        int x;
        int y;

        public Coord(final String coordPair) {
            final String[] coords = coordPair.split(",");
            this.x = Integer.parseInt(coords[0]);
            this.y = Integer.parseInt(coords[1]);
        }
    }
}
