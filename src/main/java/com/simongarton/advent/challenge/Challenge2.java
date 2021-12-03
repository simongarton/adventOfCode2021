package com.simongarton.advent.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge2 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Dive! 1";
    private static final String TITLE_2 = "Dive! 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    private int part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Coord c = new Coord(0, 0, 0);
        for (final String line : lines) {
            this.move(line, c);
        }
        final int destination = c.destination();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                destination,
                System.currentTimeMillis() - start));
        return destination;
    }

    private Coord move(final String line, final Coord c) {
        final String[] commands = line.split(" ");
        final String command = commands[0];
        final int delta = Integer.parseInt(commands[1]);
        switch (command) {
            case "forward":
                c.setX(c.getX() + delta);
                break;
            case "up":
                c.setZ(c.getZ() - delta);
                break;
            case "down":
                c.setZ(c.getZ() + delta);
                break;
            default:
                throw new RuntimeException("Unknown command " + command);
        }
        return c;
    }

    private Coord moveWithAim(final String line, final Coord c) {
        final String[] commands = line.split(" ");
        final String command = commands[0];
        final int delta = Integer.parseInt(commands[1]);
        switch (command) {
            case "forward":
                c.setX(c.getX() + delta);
                c.setZ(c.getZ() + (delta * c.getAim()));
                break;
            case "up":
                c.setAim(c.getAim() - delta);
                break;
            case "down":
                c.setAim(c.getAim() + delta);
                break;
            default:
                throw new RuntimeException("Unknown command " + command);
        }
        return c;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Coord c = new Coord(0, 0, 0);
        for (final String line : lines) {
            this.moveWithAim(line, c);
        }
        final int destination = c.destination();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                destination,
                System.currentTimeMillis() - start));
        return destination;
    }

    @Data
    @AllArgsConstructor
    private static final class Coord {
        int x;
        int z;
        int aim;

        public int destination() {
            return this.x * this.z;
        }
    }
}
