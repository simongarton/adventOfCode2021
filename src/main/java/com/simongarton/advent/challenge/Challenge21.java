package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Challenge21 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Dirac Dice 1";
    private static final String TITLE_2 = "Dirac Dice 2";

    private static final int PART2_WIN = 21;
    private static final int PART2_DEPTH = 24;

    private static final Map<Integer, Integer> moveUniverses = new HashMap<>();

    static {
        moveUniverses.put(3, 1);
        moveUniverses.put(4, 3);
        moveUniverses.put(5, 6);
        moveUniverses.put(6, 7);
        moveUniverses.put(7, 6);
        moveUniverses.put(8, 3);
        moveUniverses.put(9, 1);
    }

    public void run(final String[] lines) {
        this.part1(lines);
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
                loser = two.score;
                break;
            }
            if (two.roll3Times(die)) {
                loser = one.score;
                break;
            }
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
        final long result = this.buildOutcomeMaps(one, two);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long buildOutcomeMaps(final Player one, final Player two) {
        Map<Integer, Long> movesToWinPlayer1 = new HashMap<>();
        System.out.println(LocalTime.now() + " starting");
        this.buildOutcomeMap(one, movesToWinPlayer1);
        System.out.println(LocalTime.now() + " Player 1 done");
        Map<Integer, Long> movesToWinPlayer2 = new HashMap<>();
        this.buildOutcomeMap(two, movesToWinPlayer2);
        System.out.println(LocalTime.now() + " Player 2 done");
        long oneWins = 0;
        long twoWins = 0;
        for (final Map.Entry<Integer, Long> entry1 : movesToWinPlayer1.entrySet()) {
            for (final Map.Entry<Integer, Long> entry2 : movesToWinPlayer1.entrySet()) {
                if (entry1.getKey() <= entry2.getKey()) {
                    oneWins += entry1.getValue() + entry1.getValue();
                } else {
                    twoWins += entry1.getValue() + entry1.getValue();
                }
            }
        }
        System.out.println(LocalTime.now() + " one " + oneWins + " two " + twoWins);
        return Math.max(oneWins, twoWins);
    }

    private void buildOutcomeMap(final Player player, final Map<Integer, Long> movesToWinPlayer) {
        final int stringLength = PART2_DEPTH;
        movesToWinPlayer.clear();

        /*

        I was treating the individual rolls, but they are not important, we only consider what we got to
        on the third roll. So instead of having 3 rolls, each of which could be 1,2,3;  we have one roll,
        which has possible outcomes of 3,4,5,6,7,8,9. However, the trick is that some are more likely than others ...
        a total of 3 can only be generated in 1 universe, but 4 would be 3 universes. So I need a smaller sample size, but need
        to change the scores when I get them back.

         */

        String sequence = new String(new char[stringLength]).replace("\0", "3");
        this.playWithSequence(player, sequence, movesToWinPlayer);
        final String end = new String(new char[stringLength]).replace("\0", "9");
        while (true) {
            sequence = this.nudgeString(sequence, sequence.length() - 1);
            if (sequence.equalsIgnoreCase(end)) {
                this.playWithSequence(player, sequence, movesToWinPlayer);
                break;
            }
            this.playWithSequence(player, sequence, movesToWinPlayer);
        }
    }

    private void playWithSequence(final Player player, final String sequence, final Map<Integer, Long> movesToWinPlayer) {
        final Player newPlayer = new Player(0, player.position);
        final int moves = newPlayer.playWithDie(new SequenceDie(sequence));
        final long possibleGames = this.calculatePossibleGames(newPlayer.moveSequence);
//        System.out.println(sequence + " moves " + moves + " games " + possibleGames);
        movesToWinPlayer.put(moves, movesToWinPlayer.getOrDefault(moves, 0L) + possibleGames);
    }

    private long calculatePossibleGames(final String sequence) {
        int possible = 1;
        for (int i = 0; i < sequence.length(); i++) {
            possible = possible * moveUniverses.get(Integer.parseInt(sequence.substring(i, i + 1)));
        }
        return possible;
    }

    private String nudgeString(final String original, int position) {
        int current = Integer.parseInt(original.substring(position, position + 1)) + 1;
        boolean rollOver = false;
        if (current == 10) {
            rollOver = true;
            current = 3;
        }
        final String updated = original.substring(0, position) + current + original.substring(position + 1);
        if (!rollOver) {
            return updated;
        }
        position--;
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

        private void moveTo(final int roll) {
            this.moveSequence += roll + "";
            this.position = this.position + roll;
            while (this.position > 10) {
                this.position -= 10;
            }
        }

        @Override
        public String toString() {
            return "Player " + this.id + " (moves " + this.moves + " score " + this.score + " position " + this.position + ")";
        }

        public int playWithDie(final SequenceDie sequenceDie) {
            while (this.score < PART2_WIN) {
                this.moveTo(sequenceDie.roll());
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
}
