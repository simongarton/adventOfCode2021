package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Challenge4 {

    // done in around 58 ms
    // remove logging and done in 19 ms

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Giant Squid 1";
    private static final String TITLE_2 = "Giant Squid 2";

    public void run(final String[] lines) {
        // move board construction here to do once.
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge4() {
    }

    private int part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final List<String> numbers = Arrays.asList(lines[0].split(","));
        final List<Board> boards = this.constructBoards(lines);
        final int winningBoard = this.scoreBoards(numbers, boards);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                winningBoard,
                System.currentTimeMillis() - start));
        return winningBoard;
    }

    private int part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final List<String> numbers = Arrays.asList(lines[0].split(","));
        final List<Board> boards = this.constructBoards(lines);
        final int winningBoard = this.scoreBoardsForLast(numbers, boards);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                winningBoard,
                System.currentTimeMillis() - start));
        return winningBoard;
    }

    private int scoreBoards(final List<String> numbers, final List<Board> boards) {
        for (int i = 0; i < numbers.size(); i++) {
            final String move = numbers.get(i);
//            System.out.println("move " + i + " '" + move + "'");
            for (final Board board : boards) {
                if (board.winningMove(move, i)) {
                    return board.score(Integer.parseInt(move));
                }
            }
        }
        throw new RuntimeException("No winner.");
    }

    private int scoreBoardsForLast(final List<String> numbers, final List<Board> boards) {
        int boardsWon = 0;
        for (int i = 0; i < numbers.size(); i++) {
            final String move = numbers.get(i);
//            System.out.println("move " + i + " " + move);
            for (final Board board : boards) {
                if (!board.inPlay) {
                    continue;
                }
                if (board.winningMove(move, i)) {
                    boardsWon++;
                    if (boardsWon == boards.size()) {
                        return board.score(Integer.parseInt(move));
                    }
                }
            }
        }
        throw new RuntimeException("No winner.");
    }

    private List<Board> constructBoards(final String[] lines) {
        final List<Board> boards = new ArrayList<>();
        final int boardCount = (lines.length - 1) / 6;
        int startRow = 1;
        for (int boardId = 0; boardId < boardCount; boardId++) {
            final Board board = new Board(boardId);
            for (int row = 1; row < 6; row++) {
                board.addRow(lines[startRow + row], row - 1);
            }
            boards.add(board);
            startRow += 6;
        }
        return boards;
    }

    private static final class Board {

        int id;
        boolean inPlay;
        String[] squares = new String[25];
        boolean[] scores = new boolean[25];

        public Board(final int id) {
            this.id = id;
            this.inPlay = true;
        }

        public void addRow(final String line, final int row) {
            final String cleanLine = line.trim().replace("  ", " ");
            final String[] numbers = cleanLine.split(" ");
            for (int i = 0; i < 5; i++) {
                this.squares[(row * 5) + i] = numbers[i];
            }
        }

        public void printBoard() {
            for (int row = 0; row < 5; row++) {
                final StringBuilder line = new StringBuilder();
                for (int col = 0; col < 5; col++) {
                    line.append(this.padTo(this.squares[(row * 5) + col], 2)).append(" ");
                }
                System.out.println(line);
            }
        }

        public void printBoardScores() {
            for (int row = 0; row < 5; row++) {
                final StringBuilder line = new StringBuilder();
                for (int col = 0; col < 5; col++) {
                    if (this.scores[(row * 5) + col]) {
                        line.append(" * ");
                    } else {
                        line.append(" . ");
                    }
                }
                System.out.println(line);
            }
        }

        public boolean winningMove(final String move, final int moveId) {
            for (int i = 0; i < 25; i++) {
                if (this.squares[i].equalsIgnoreCase(move)) {
                    this.scores[i] = true;
                }
            }
            // no way we can win in less than 5 moves.
            if (moveId < 5) {
                return false;
            }
            for (int row = 0; row < 5; row++) {
                if (this.scores[(row * 5) + 0] &&
                        this.scores[(row * 5) + 1] &&
                        this.scores[(row * 5) + 2] &&
                        this.scores[(row * 5) + 3] &&
                        this.scores[(row * 5) + 4]) {
//                    System.out.println("win on board " + this.id + " move " + moveId + " ('" + move + "') row");
//                    this.printBoard();
//                    this.printBoardScores();
                    this.inPlay = false;
                    return true;
                }
            }
            for (int col = 0; col < 5; col++) {
                if (this.scores[col + 0] &&
                        this.scores[col + 5] &&
                        this.scores[col + 10] &&
                        this.scores[col + 15] &&
                        this.scores[col + 20]) {
//                    System.out.println("win on board " + this.id + " move " + moveId + " ('" + move + "') col");
//                    this.printBoard();
//                    this.printBoardScores();
                    this.inPlay = false;
                    return true;
                }
            }
            return false;
        }

        public int score(final int lastMove) {
            int total = 0;
            for (int i = 0; i < 25; i++) {
                if (!this.scores[i]) {
                    total += Integer.parseInt(this.squares[i]);
                }
            }
//            System.out.println("unscored " + total + " * move '" + lastMove + "' = " + total * lastMove);
            return total * lastMove;
        }

        private String padTo(final String s, final int size) {
            if (s.length() > size) {
                return new String(new char[size]).replace("\0", "*");
            }
            return new String(new char[size - s.length()]).replace("\0", " ") + s;
        }
    }
}
