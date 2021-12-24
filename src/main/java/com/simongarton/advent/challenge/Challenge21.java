package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge21 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Dirac Dice 1";
    private static final String TITLE_2 = "Dirac Dice 2";

    public void run(final String[] lines) {
//        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Player one = new Player(1, this.getPosition(lines[0]));
        final Player two = new Player(2, this.getPosition(lines[1]));
        final Die die = new DeterministicDie();
        final long loser;
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

    private int getPosition(final String line) {
        final String[] parts = line.split(": ");
        return Integer.parseInt(parts[1]);
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Player one = new Player(1, this.getPosition(lines[0]));
        final Player two = new Player(2, this.getPosition(lines[1]));
        final GameState gameState = new GameState(one.makeCopy(), two.makeCopy());
        gameState.kablooie();
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

        public Player(final int id, final int position) {
            this.id = id;
            this.score = 0;
            this.moves = 0;
            this.position = position;
        }

        public boolean roll3Times(final Die die) {
            final int roll1 = die.roll();
            final int roll2 = die.roll();
            final int roll3 = die.roll();
            this.moveTo(roll1 + roll2 + roll3);
            this.score += this.position;
            this.moves += 1;
            return (this.score >= 1000);
        }

        private int moveTo(final int roll) {
//            System.out.println("got roll " + roll + " was at position " + this.position);
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

        public Player makeCopy() {
            final Player player = new Player(this.id, this.position);
            player.score = this.score;
            player.moves = this.moves;
            return player;
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

    public static final class GameState {

        // I need to hold the state of the game - which will include the two players, their positions, scores,
        // and then whose turn it is and which roll it is of 3.
        // each state will then have three child states - moving onto the next roll, or turn, and checking to see if someone has won.

        private static long counter = 0;
        private static long kablooies = 0;
        private static long oneWins = 0;
        private static long twoWins = 0;

        private final long id;
        private final Player one;
        private final Player two;
        private boolean oneToPlay;
        private int roll;  // this is the next roll to make
        private boolean complete;
        private int winner;

        private GameState child1;
        private GameState child2;
        private GameState child3;

        public GameState(final Player one, final Player two) {
            this.id = counter++;
            this.oneToPlay = true;
            this.roll = 1;
            this.complete = false;
            this.winner = 0;
            this.one = one;
            this.two = two;
        }

        public void kablooie() {
            if (kablooies % 1000000 == 0) {
                System.out.println("kablooie for " + this.id + " on " + kablooies + " one wins " + oneWins + " two wins " + twoWins);
            }
            kablooies++;
//            System.out.println(" " + this.one);
//            System.out.println(" " + this.two);
            // if I have won already, then stop
            if (this.complete) {
//                System.out.println("kablooie for " + this.id + " on " + kablooies++ + " one wins " + oneWins + " two wins " + twoWins);
                return;
            }
            this.child1 = new GameState(this.one.makeCopy(), this.two.makeCopy());
            this.child2 = new GameState(this.one.makeCopy(), this.two.makeCopy());
            this.child3 = new GameState(this.one.makeCopy(), this.two.makeCopy());
            this.child1.progressGame(this, 1);
            this.child2.progressGame(this, 2);
            this.child3.progressGame(this, 3);
            this.child1.kablooie();
            this.child2.kablooie();
            this.child3.kablooie();
        }

        private void progressGame(final GameState parent, final int diceRoll) {
            this.oneToPlay = parent.oneToPlay;
            this.roll = parent.roll + 1;
            if (this.roll == 4) {
                this.oneToPlay = !this.oneToPlay;
            }
            if (this.oneToPlay) {
                this.one.moveTo(diceRoll);
                if (this.roll == 4) {
                    this.one.score += this.one.position;
                    this.one.moves += 1;
                    if (this.one.score >= 21) {
//                        System.out.println("one won with a score of " + this.one.score + " on move " + this.one.moves);
                        this.complete = true;
                        this.winner = 1;
                        oneWins++;
                    }
                }
            } else {
                this.two.moveTo(diceRoll);
                if (this.roll == 4) {
                    this.two.score += this.two.position;
                    this.two.moves += 1;
                    if (this.two.score >= 21) {
//                        System.out.println("two won with a score of " + this.two.score + " on move " + this.two.moves);
                        this.complete = true;
                        this.winner = 2;
                        twoWins++;
                    }
                }
            }
            if (this.roll == 4) {
                this.roll = 1;
            }
        }
    }
}
