package com.simongarton.advent.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Coord c = new Coord(0,0);
        for (String line : lines) {
            c = move(line, c);
        }
        int destination = c.destination();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                destination,
                System.currentTimeMillis() - start));
        return destination;
    }

    private Coord move(String line, Coord c) {
        String[] commands = line.split(" ");
        String command = commands[0];
        int delta = Integer.parseInt(commands[1]);
        switch(command) {
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

    private long part2(final String[] lines) {
        return 0;
    }

    @Data
    @AllArgsConstructor
    private static final class Coord {
        int x;
        int z;

        public int destination() {
            return x * z;
        }
    }
}
