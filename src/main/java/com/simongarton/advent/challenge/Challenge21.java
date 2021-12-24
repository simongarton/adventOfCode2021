package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge21 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Dirac Dice 1";
    private static final String TITLE_2 = "Dirac Dice 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        Player one = new Player(1, getPosition(lines[0]));
        Player two = new Player(2, getPosition(lines[1]));
        Die die = new DeterministicDie();
        long loser;
        while (true) {
            if (one.roll3Times(die)) {
                System.out.println(one + " wins.");
                loser = two.score;
                break;
            }
            System.out.println(one);
            if (two.roll3Times(die)) {
                System.out.println(two + " wins.");
                loser = one.score;
                break;
            }
            System.out.println(two);
        }
        final long result = (3L * (one.moves + two.moves)) * loser;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private int getPosition(String line) {
        String[] parts = line.split(": ");
        return Integer.parseInt(parts[1]);
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

    public static final class Player {
        int id;
        int score;
        int position;
        int moves;

        public Player(final int id, int position) {
            this.id = id;
            this.score = 0;
            this.moves = 0;
            this.position = position;
        }

        public boolean roll3Times(final Die die) {
            int roll1 = die.roll();
            int roll2 = die.roll();
            int roll3 = die.roll();
            this.moveTo(roll1 + roll2 + roll3);
            this.score += this.position;
            this.moves += 1;
            return (this.score >= 1000);
        }

        private int moveTo(final int roll) {
            System.out.println("got roll " + roll + " was at position " + position);
            this.position = this.position + roll;
            while (this.position > 10) {
                this.position -= 10;
            }
            return this.position;
        }

        @Override
        public String toString() {
            return "Player " + this.id + " (moves " + this.moves + " score " + this.score + " position " + this.position + ")";
        }
    }

    public interface Die {

        int roll();
    }

    public static final class DeterministicDie implements Die {

        int value;

        public DeterministicDie() {
            this.value = 1;
        }

        @Override
        public int roll() {
            final int result = this.value;
            this.value++;
            if (this.value > 100) {
                this.value = 1;
            }
            System.out.println("->" + result);
            return result;
        }
    }
}
