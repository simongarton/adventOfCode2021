package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Challenge23 {

    private static final String TITLE_1 = "Amphipod 1";
    private static final String TITLE_2 = "Amphipod 2";

    private static final int columnA = 3;
    private static final int columnB = 5;
    private static final int columnC = 7;
    private static final int columnD = 9;
    private static final int sideRoomRow = 2;

    private static final Map<String, String> names = new HashMap<>();

    static {
        names.put("A", "Amber");
        names.put("B", "Bronze");
        names.put("C", "Copper");
        names.put("D", "Desert");
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private int width;
    private int height;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final String map = this.loadBurrow(lines);
        this.printStringAsMap(map);
        this.mapOutGraph(map);
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_1,
                System.currentTimeMillis() - start));
        return result;
    }

    private void mapOutGraph(final String map) {
        final List<String> nodes = new ArrayList<>();
        this.analyseMap(map);
        System.out.println("");
        final String[] rows = this.stringToMap(map);
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                final String symbol = "" + rows[row].charAt(col);
                if (this.isAmphipod(symbol)) {
                    final List<String> possibleMoves = this.getPossibleMoves(map, row, col, symbol);
                    for (final String possibleMove : possibleMoves) {
                        this.printStringAsMap(possibleMove);
                        System.out.println("");
                    }
                }
            }
        }
    }

    private List<String> getPossibleMoves(final String map, final int row, final int col, final String symbol) {
        final List<String> possibleMoves = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if ((x != 0)  && (y != 0)) {
                    continue;
                }
                final String possibleMove = this.getPossibleMove(map, row, col, row + y, col + x, symbol);
                if (possibleMove != null) {
                    possibleMoves.add(possibleMove);
                }
            }
        }
        return possibleMoves;
    }

    private String getPossibleMove(final String map, final int oldRow, final int oldCol, final int newRow, final int newCol, final String oldSymbol) {
        if (newRow < 0 || newRow >= this.height) {
            return null;
        }
        if (newCol < 0 || newCol >= this.width) {
            return null;
        }
        final String[] rows = this.stringToMap(map);
        final String symbol = "" + rows[newRow].charAt(newCol);
        if (!symbol.equalsIgnoreCase(".")) {
            return null;
        }
        if (!legalMove(map, oldRow, oldCol, newRow, newCol, oldSymbol)) {
            return null;
        }
        System.out.println("Trying to move " + oldSymbol + " at " + oldCol + "," + oldRow + " to " + newCol + "," + newRow);
        final String[] changedRows = new String[this.height];
        for (int row = 0; row < this.height; row++) {
            if (row != oldRow && row != newRow) {
                changedRows[row] = rows[row];
                continue;
            }
            String newLine = rows[row];
            if (row == oldRow) {
                newLine = newLine.substring(0, oldCol) + "." + newLine.substring(oldCol + 1);
            }
            if (row == newRow) {
                newLine = newLine.substring(0, newCol) + oldSymbol + newLine.substring(newCol + 1);
            }
            changedRows[row] = newLine;
        }
        return this.mapToString(changedRows);
    }

    private boolean legalMove(String map, int oldRow, int oldCol, int newRow, int newCol, String oldSymbol) {
        // can't stop on a space outside a room - translate this as if I have an Amphipod outside a room and it's NOT the one moving, the move isn't legal

        // can't move into a side room it's not it's own; and if it's got any other kind of amphipod in it

        // once stopped in the hallway, will not move again unless going to it's room

        /*

        This last one has broken my approach of moving them one square at a time. I need to test moving them to ALL vacant squares in one go,
        and that means checking to see if the route is blocked.

        Harrumph.

         */

        return true;
    }

    private boolean isAmphipod(final String symbol) {
        return symbol.equalsIgnoreCase("A") ||
                symbol.equalsIgnoreCase("B") ||
                symbol.equalsIgnoreCase("C") ||
                symbol.equalsIgnoreCase("D");
    }

    private void analyseMap(final String map) {
        final String[] rows = this.stringToMap(map);
        for (int row = 0; row < this.height; row++) {
            for (int col = 0; col < this.width; col++) {
                final String symbol = "" + rows[row].charAt(col);
                System.out.println(col + "," + row + "  (" + symbol + ") : " + this.explainSymbol(col, row, symbol));
            }
        }
    }

    private String explainSymbol(final int col, final int row, final String symbol) {
        if (symbol.equalsIgnoreCase(" ")) {
            return "outside";
        }
        if (symbol.equalsIgnoreCase("#")) {
            return "wall";
        }
        if (symbol.equalsIgnoreCase(".")) {
            return "vacant " + this.specialPosition(col, row, symbol);
        }
        return names.get(symbol) + " in " + this.specialPosition(col, row, symbol);
    }

    private String specialPosition(final int col, final int row, final String symbol) {
        if (row == sideRoomRow - 1) {
            return "tunnel";
        }
        if (row < (sideRoomRow - 1)) {
            return "";
        }
        if (row == this.height - 1) {
            return "";
        }
        if (
                (col == columnA) ||
                        (col == columnB) ||
                        (col == columnC) ||
                        (col == columnD)
        ) {
            return names.get(symbol) + " room";
        }
        return "";
    }

    private String mapToString(final String[] rows) {
        final StringBuilder map = new StringBuilder();
        for (final String row : rows) {
            map.append(row);
        }
        return map.toString();
    }

    private String[] stringToMap(final String string) {
        final String[] map = new String[this.height];
        for (int i = 0; i < this.height; i++) {
            map[i] = string.substring(i * this.width, (i + 1) * this.width);
        }
        return map;
    }

    private void printStringAsMap(final String string) {
        final String[] map = this.stringToMap(string);
        for (final String line : map) {
            System.out.println(line);
        }
    }

    private String loadBurrow(final String[] lines) {
        this.width = lines[0].length();
        this.height = lines.length;
        final String[] rows = new String[lines.length];
        int row = 0;
        for (final String line : lines) {
            rows[row++] = this.padLineToFullLength(line, this.width);
        }
        return this.mapToString(rows);
    }

    private String padLineToFullLength(final String line, final int width) {
        if (line.length() == width) {
            return line;
        }
        return line + new String(new char[width - line.length()]).replace("\0", " ");
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_2,
                System.currentTimeMillis() - start));
        return result;
    }
}
