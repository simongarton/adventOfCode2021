package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Challenge25 {

    private static final String TITLE_1 = "Sea Cucumber 1";
    private static final String TITLE_2 = "Sea Cucumber 2";
    private static final int EAST = 1;
    private static final int SOUTH = 2;
    private static final int NOTHING = 0;

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private int[] floor;
    private int width;
    private int height;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        this.width = lines[0].length();
        this.height = lines.length;
        this.floor = new int[this.width * this.height];
        this.loadFloor(lines);
        final long result = this.iterateUntilBlocked();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long iterateUntilBlocked() {
        int cycles = 0;
        while (true) {
            int moves = this.moveEast();
            moves = moves + this.moveSouth();
            cycles++;
            if (moves == 0) {
                break;
            }
        }
        return cycles;
    }

    private int moveSouth() {
        return this.move(SOUTH);
    }

    private int moveEast() {
        return this.move(EAST);
    }

    private int move(final int direction) {
        final List<SeaCucumber> seaCucumbers = new ArrayList<>();
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                final int val = this.floor[(row * this.width) + col];
                if (val == direction) {
                    seaCucumbers.add(new SeaCucumber(row, col, direction));
                }
            }
        }
        for (final SeaCucumber seaCucumber : seaCucumbers) {
            seaCucumber.explore(this.width, this.height, this.floor);
        }
        int moves = 0;
        for (final SeaCucumber seaCucumber : seaCucumbers) {
            if (seaCucumber.canMove) {
                this.floor[(seaCucumber.row * this.width) + seaCucumber.col] = NOTHING;
                this.floor[(seaCucumber.nextRow * this.width) + seaCucumber.nextCol] = direction;
                moves++;
            }
        }
        return moves;
    }

    private void loadFloor(final String[] lines) {
        int index = 0;
        for (final String line : lines) {
            for (int i = 0; i < this.width; i++) {
                switch (line.charAt(i)) {
                    case '>':
                        this.floor[index++] = EAST;
                        break;
                    case 'v':
                        this.floor[index++] = SOUTH;
                        break;
                    case '.':
                        this.floor[index++] = NOTHING;
                        break;
                }
            }
        }
    }

    private void printFloor() {
        for (int row = 0; row < this.height; row++) {
            final StringBuilder line = new StringBuilder();
            for (int col = 0; col < this.width; col++) {
                final int val = this.floor[(row * this.width) + col];
                switch (val) {
                    case EAST:
                        line.append(">");
                        break;
                    case SOUTH:
                        line.append("v");
                        break;
                    case NOTHING:
                        line.append(".");
                        break;
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_2,
                System.currentTimeMillis() - start));
        return result;
    }

    public static final class SeaCucumber {
        final int row;
        final int col;
        int nextRow;
        int nextCol;
        final int direction;
        boolean canMove;

        public SeaCucumber(final int row, final int col, final int direction) {
            this.row = row;
            this.col = col;
            this.direction = direction;
            this.canMove = false;
        }

        public void explore(final int width, final int height, final int[] floor) {
            this.nextRow = this.row;
            this.nextCol = this.col;
            if (this.direction == EAST) {
                this.nextCol = (this.nextCol + 1) % width;
            } else {
                this.nextRow = (this.nextRow + 1) % height;
            }
            final int val = floor[(this.nextRow * width) + this.nextCol];
            this.canMove = val == NOTHING;
        }
    }
}
