package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Challenge21 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private Map<Integer, Long> movesToWinPlayer1;
    private Map<Integer, Long> movesToWinPlayer2;

    private static final String TITLE_1 = "Dirac Dice 1";
    private static final String TITLE_2 = "Dirac Dice 2";

    /* I have a idea.

    Take player 1 and run it recursively through all possibly games, finding out which move he finishes on
    and building up a map of moves:times_found

    Repeat for player 2.

    Loop over all combinations and count up how many times 1 would have beaten 2.
     */

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
//        final GameState gameState = new GameState(one.makeCopy(), two.makeCopy());
//        gameState.kablooie();
        this.buildOutcomeMaps(one, two);
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private void buildOutcomeMaps(final Player one, final Player two) {
        this.movesToWinPlayer1 = new HashMap<>();
        this.buildOutcomeMap(one, this.movesToWinPlayer1);
        System.out.println(LocalTime.now() + " Player 1 done");
        this.movesToWinPlayer2 = new HashMap<>();
        this.buildOutcomeMap(two, this.movesToWinPlayer2);
        System.out.println(LocalTime.now() + " Player 2 done");
        long oneWins = 0;
        long twoWins = 0;
        for (final Map.Entry<Integer, Long> entry1 : this.movesToWinPlayer1.entrySet()) {
            for (final Map.Entry<Integer, Long> entry2 : this.movesToWinPlayer1.entrySet()) {
                if (entry1.getKey() <= entry2.getKey()) {
                    oneWins += entry1.getValue() + entry1.getValue();
                } else {
                    twoWins += entry1.getValue() + entry1.getValue();
                }
            }
        }
        System.out.println(LocalTime.now() + " Done one " + oneWins + " two " + twoWins);
    }

    private void buildOutcomeMap(final Player one, final Map<Integer, Long> movesToWinPlayer) {
        final int stringLength = 24;
        movesToWinPlayer.clear();

        String original = new String(new char[stringLength]).replace("\0", "1");
        this.playWithSequence(one, original, movesToWinPlayer);
        final String end = new String(new char[stringLength]).replace("\0", "3");
        while (true) {
            original = this.nudgeString(original, original.length() - 1);
            if (original.equalsIgnoreCase(end)) {
                this.playWithSequence(one, original, movesToWinPlayer);
                break;
            }
            this.playWithSequence(one, original, movesToWinPlayer);
        }
    }

    private void playWithSequence(final Player one, final String sequence, final Map<Integer, Long> movesToWinPlayer) {
        final Player player = new Player(0, one.position);
        final int moves = player.playWithDie(new SequenceDie(sequence));
        movesToWinPlayer.put(moves, movesToWinPlayer.getOrDefault(moves, 0L) + 1);
    }

    private String nudgeString(final String original, int position) {
//        System.out.println("nudging " +  original + " at " + position);
        int current = Integer.parseInt(original.substring(position, position + 1)) + 1;
        boolean rollOver = false;
        if (current == 4) {
            rollOver = true;
            current = 1;
        }
        final String updated = original.substring(0, position) + current + original.substring(position + 1);
        if (!rollOver) {
//            System.out.println("done nudging " +  updated + " at " + position);
            return updated;
        }
        position--;
        if (position == -1) {
            // this never executes ...
//            System.out.println("all finished with " + "3" + updated.substring(1));
            return "3" + updated.substring(1);
        }
//        System.out.println("going to nudge " + updated + " at " + position);
        return this.nudgeString(updated, position);
    }

    public static final class Player {
        int id;
        int score;
        int position;
        int moves;
        String moveSequence = "";

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
            this.moveSequence += roll + ",";
            //System.out.println("player " + this.id + " got roll " + roll + " was at position " + this.position + " : " + this.moveSequence + " and score " + this.score);
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
            player.moveSequence = this.moveSequence;
            return player;
        }

        public int playWithDie(final SequenceDie sequenceDie) {
            while (this.score <= 21) {
                final int roll1 = sequenceDie.roll();
                final int roll2 = sequenceDie.roll();
                final int roll3 = sequenceDie.roll();
                this.moveTo(roll1 + roll2 + roll3);
                this.score += this.position;
                this.moves += 1;
            }
            return this.moves;
        }
    }

    public interface Die {

        int roll();
    }

    public static final class SequenceDie implements Die {

        String sequence;
        int index;

        public SequenceDie(final String sequence) {
            this.sequence = sequence;
            this.index = 0;
        }

        @Override
        public int roll() {
            if (this.index >= this.sequence.length()) {
                throw new RuntimeException("need more rolls : index " + this.index + " with " + this.sequence);
            }
            final int result = Integer.parseInt(this.sequence.substring(this.index, this.index + 1));
            this.index++;
            return result;
        }
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

    public static final class OutcomeGenerator {

        private static long counter = 0;
        private static long kablooies = 0;

        private long id = 0;

        private final Map<Integer, Long> movesToWinPlayer;

        private final Player player;
        private int roll;  // this is the next roll to make
        private boolean complete;
        private String moveSequence;

        private OutcomeGenerator child1;
        private OutcomeGenerator child2;
        private OutcomeGenerator child3;

        public OutcomeGenerator(final Player player, final Map<Integer, Long> movesToWinPlayer) {
            this.id = ++counter;
            this.roll = 1;
            this.complete = false;
            this.player = player;
            this.movesToWinPlayer = movesToWinPlayer;
        }

        public void kablooie() {
            System.out.println("kablooie for " + this.id + " on " + kablooies);
            if (this.complete) {
                return;
            }
            if (kablooies % 1000000 == 0) {
//                System.out.println("kablooie for " + this.id + " on " + kablooies + " one wins " + oneWins);
            }
            kablooies++;
            this.child1 = new OutcomeGenerator(this.player.makeCopy(), this.movesToWinPlayer);
            this.child2 = new OutcomeGenerator(this.player.makeCopy(), this.movesToWinPlayer);
            this.child3 = new OutcomeGenerator(this.player.makeCopy(), this.movesToWinPlayer);
            this.child1.progressGame(this, 1);
            this.child2.progressGame(this, 2);
            this.child3.progressGame(this, 3);
        }

        private void progressGame(final OutcomeGenerator parent, final int diceRoll) {
            this.roll = parent.roll + 1;
            this.player.moveTo(diceRoll);
            if (this.roll == 4) {
                this.player.score += this.player.position;
                this.player.moves += 1;
                // any higher than 18 goes badly
                if (this.player.score >= 21) {
                    this.complete = true;
                    this.movesToWinPlayer.put(this.player.moves, this.movesToWinPlayer.getOrDefault(this.player.moves, 0L) + 1);
                    return;
                }
                this.roll = 1;
            }
            this.kablooie();
        }
    }
}
