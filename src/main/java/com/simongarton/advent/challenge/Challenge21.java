package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class Challenge21 {

    private static final int TRACK_END = 10;
    private static final int PART2_WIN = 21;

    private static final String TITLE_1 = "Dirac Dice 1";
    private static final String TITLE_2 = "Dirac Dice 2";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final Map<Integer, Integer> moveUniverses = new HashMap<>();
    private final Map<Integer, Long> wins = new HashMap<>();

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
//        final long result = this.buildOutcomeMaps(one, two);
        this.wins.clear();
        this.wins.put(1, 0L);
        this.wins.put(2, 0L);
        this.playAGame(one.id, one.position, one.score, two.id, two.position, two.score, 1);
        final long result;
        if (this.wins.get(1) > this.wins.get(2)) {
            result = this.wins.get(1) - this.wins.get(2);
        } else {
            result = this.wins.get(2) - this.wins.get(1);
        }
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private void playAGame(final int aId, int aPos, int aScore, final int bId, final int bPos, final int bScore, final long gameTreeCount) {
        // I'm going to call this recursively. For each outcome
        // I multiply the gameTreeCount and play for player a
        for (final Map.Entry<Integer, Integer> entry : moveUniverses.entrySet()) {
            aPos += entry.getKey();
            aPos = aPos % 10;
            aScore += aPos;
            if (aScore >= 21) {
                this.wins.put(aId, this.wins.get(aId) + gameTreeCount);
                return;
            }
            this.playAGame(bId, bPos, bScore, aId, aPos, aScore, gameTreeCount * entry.getValue());
        }
    }

    private long buildOutcomeMaps(final Player one, final Player two) {
        final Map<Integer, Long> movesToWinPlayer1 = new HashMap<>();
//        System.out.println(LocalTime.now() + " starting");
        this.buildOutcomeMap(one, movesToWinPlayer1);
//        System.out.println(LocalTime.now() + " Player 1 done");
        final Map<Integer, Long> movesToWinPlayer2 = new HashMap<>();
        this.buildOutcomeMap(two, movesToWinPlayer2);
//        System.out.println(LocalTime.now() + " Player 2 done");
        long oneWins = 0;
        long twoWins = 0;
        System.out.println("Player 1");
        for (final Map.Entry<Integer, Long> entry1 : movesToWinPlayer1.entrySet()) {
            System.out.println(entry1.getKey() + ":" + entry1.getValue());
        }
        System.out.println("Player 2");
        for (final Map.Entry<Integer, Long> entry2 : movesToWinPlayer2.entrySet()) {
            System.out.println(entry2.getKey() + ":" + entry2.getValue());
        }
        for (final Map.Entry<Integer, Long> entry1 : movesToWinPlayer1.entrySet()) {
            for (final Map.Entry<Integer, Long> entry2 : movesToWinPlayer2.entrySet()) {
                if (entry1.getKey() <= entry2.getKey()) {
                    oneWins += entry1.getValue() * entry2.getValue();
                } else {
                    twoWins += entry1.getValue() * entry2.getValue();
                }
            }
        }
        System.out.println(LocalTime.now() + " one " + oneWins + " two " + twoWins);
        return Math.max(oneWins, twoWins);
    }

    private void buildOutcomeMap(final Player player, final Map<Integer, Long> movesToWinPlayer) {

        /*

        Ok, round ... 7 ? Having first gone down recursion and then left it as too big and then realised
        that the 3 rolls is a red herring ... can I do recursion again ? I have tried various attempts and am getting
        wildly varying results ... so a fresh start.

        I still like the outcome maps. For each player, work out how many outcomes there possibly are (how many moves to win);
        and then count up how many pairs of moves there are where player 1 reaches it that move or not.

        My first triple throw will give me 27 possible outcomes - values 3 to 9 moves, but some are repeated.
        Then my second throw is another 27 outcomes, the combination is 729 paired moves, but actually only 49 different strings ...
        Keep building up the strings until I get a win

        It all looks sensible. I get outcomes of this ...

Player 1
3:4608
4:249542
5:3219454
Player 2
3:1730
4:230681
5:5448341

    however, if I multiply them out, I end up with

18,984,014,638,954	96.21%
748,668,231,254	3.79%

i.e. player 1 wins 96%  of the time, which isn't right - should be 56% And I've only got 1/50th of the games

No, that multiplication isn't right. I can't multiply 1 x 2 because those are only the games where 2 actually finished.
Or is it ?

Reading some of the other solutions, they seem to be playing the games with both players at the same time.


https://github.com/SwampThingTom/AoC2021/blob/main/Python/21-DiracDice/DiracDice.py has an elegant solution.


         */

        movesToWinPlayer.clear();
        this.recurseStrings("", player, movesToWinPlayer);
    }

    private void recurseStrings(final String sequence, final Player player, final Map<Integer, Long> movesToWinPlayer) {
        if (sequence.length() > 5) {
            return;
        }
        boolean successful = false;
        try {
            this.playWithSequence(player, sequence, movesToWinPlayer);
            successful = true;
        } catch (final RuntimeException rte) {

        }
        if (successful) {
            return;
        }
        this.recurseStrings(sequence + "3", player, movesToWinPlayer);
        this.recurseStrings(sequence + "4", player, movesToWinPlayer);
        this.recurseStrings(sequence + "5", player, movesToWinPlayer);
        this.recurseStrings(sequence + "6", player, movesToWinPlayer);
        this.recurseStrings(sequence + "7", player, movesToWinPlayer);
        this.recurseStrings(sequence + "8", player, movesToWinPlayer);
        this.recurseStrings(sequence + "9", player, movesToWinPlayer);
    }

    private WinRecord playWithSequence(final Player player, final String sequence, final Map<Integer, Long> movesToWinPlayer) {
        final Player newPlayer = new Player(0, player.position);
        final int moves = newPlayer.playWithDie(new SequenceDie(sequence));
        final long possibleGames = this.calculatePossibleGames(sequence);
        //  System.out.println("starting at position " + player.position + " I found a win with sequence " + sequence + " moves " + moves + " (repeated in " + possibleGames + " games.)");
        movesToWinPlayer.put(moves, movesToWinPlayer.getOrDefault(moves, 0L) + possibleGames);
        return new WinRecord(sequence, possibleGames);
    }

    public static final class WinRecord {
        String sequence;
        long possibleGames;

        public WinRecord(final String sequence, final long possibleGames) {
            this.sequence = sequence;
            this.possibleGames = possibleGames;
        }
    }

    private long calculatePossibleGames(final String sequence) {
        int possible = 1;
        for (int i = 0; i < sequence.length(); i++) {
            possible = possible * moveUniverses.get(Integer.parseInt(sequence.substring(i, i + 1)));
        }
        return possible;
    }

    public static final class Player {
        int id;
        int score;
        int position;
        int moves;
        int lastRoll;

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
            this.position = this.position + roll;
            while (this.position > TRACK_END) {
                this.position -= TRACK_END;
            }
        }

        @Override
        public String toString() {
            return "Player " + this.id + " (moves " + this.moves + " score " + this.score + " position " + this.position + " last roll " + this.lastRoll + ")";
        }

        public int playWithDie(final SequenceDie sequenceDie) {
//            System.out.println(this);
            while (this.score < PART2_WIN) {
                this.lastRoll = sequenceDie.roll();
                this.moveTo(this.lastRoll);
                this.score += this.position;
                this.moves += 1;
//                System.out.println(this);
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
            return result;
        }
    }
}
