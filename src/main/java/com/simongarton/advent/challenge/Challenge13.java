package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Challenge13 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private List<String> folds;

    private static final String TITLE_1 = "Transparent Origami 1";
    private static final String TITLE_2 = "Transparent Origami 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge13() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Page page = this.loadPaper(lines);
        this.loadFolds(lines);
        final Page finalPage = this.fold(page, true);
        final long result = finalPage.score();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private Page fold(final Page page, boolean part1) {
        Page currentPage = page;
        for (final String fold : this.folds) {
            System.out.println(fold);
            if (fold.contains("fold along x")) {
                currentPage = this.foldPageAlongX(fold, currentPage);
            } else {
                currentPage = this.foldPageAlongY(fold, currentPage);
            }
            currentPage.printGrid();
            if (part1) {
                break;
            }
        }
        return currentPage;
    }

    private Page foldPageAlongY(final String fold, final Page currentPage) {
        final String[] parts = fold.split("=");
        final int foldLine = Integer.parseInt(parts[1]);
        final Page newPage = new Page(currentPage.width, currentPage.height / 2);
        for (int y = 0; y < currentPage.height; y++) {
            for (int x = 0; x < currentPage.width; x++) {
                // I think we simply skip the fold line, losing a line
                int y1 = y;
                if (y > foldLine) {
                    y1 = foldLine - (y - foldLine);
                }
                if (currentPage.getCoord(x, y)) {
                    newPage.setCoord(x, y1);
                }
            }
        }
        return newPage;
    }

    private Page foldPageAlongX(final String fold, final Page currentPage) {
        final String[] parts = fold.split("=");
        final int foldLine = Integer.parseInt(parts[1]);
        final Page newPage = new Page(currentPage.width / 2, currentPage.height);
        for (int y = 0; y < currentPage.height; y++) {
            for (int x = 0; x < currentPage.width; x++) {
                // I think we simply skip the fold line, losing a line
                int x1 = x;
                if (x > foldLine) {
                    x1 = foldLine - (x - foldLine);
                }
                if (currentPage.getCoord(x, y)) {
                    newPage.setCoord(x1, y);
                }
            }
        }
        return newPage;
    }

    private void loadFolds(final String[] lines) {
        this.folds = new ArrayList<>();
        boolean skip = true;
        for (final String line : lines) {
            if (line.length() == 0) {
                skip = false;
                continue;
            }
            if (skip) {
                continue;
            }
            this.folds.add(line);
        }
    }

    private Page loadPaper(final String[] lines) {
        int width = 0;
        int height = 0;
        final List<Coord> coords = new ArrayList<>();
        for (final String line : lines) {
            if (line.length() == 0) {
                break;
            }
            final Coord coord = new Coord(line);
            if (coord.x > width) {
                width = coord.x;
            }
            if (coord.y > height) {
                height = coord.y;
            }
            coords.add(coord);
        }
        // coords are zero based
        final Page page = new Page(++width, ++height);
        page.loadCoords(coords);
        page.printGrid();
        return page;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final Page page = this.loadPaper(lines);
        this.loadFolds(lines);
        final Page finalPage = this.fold(page, false);
        final long result = finalPage.score();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    public static class Page {
        private final boolean[] grid;
        private final int width;
        private final int height;

        public Page(final int width, final int height) {
            this.width = width;
            this.height = height;
            this.grid = new boolean[this.height * this.width];
        }

        public void loadCoords(final List<Coord> coords) {
            for (final Coord coord : coords) {
                final int index = (coord.y * this.width) + coord.x;
                this.grid[index] = true;
            }
        }

        public void printGrid() {
            for (int y = 0; y < this.height; y++) {
                final StringBuilder line = new StringBuilder();
                for (int x = 0; x < this.width; x++) {
                    if (this.grid[(y * this.width) + x]) {
                        line.append("#");
                    } else {
                        line.append(".");
                    }
                }
                System.out.println(line);
            }
        }

        public void setCoord(final int x, final int y) {
            final int index = (y * this.width) + x;
            this.grid[index] = true;
        }

        public boolean getCoord(final int x, final int y) {
            final int index = (y * this.width) + x;
            return this.grid[index];
        }

        public long score() {
            long score = 0;
            // stream this
            for (int index = 0; index < this.grid.length; index++) {
                if (this.grid[index]) {
                    score++;
                }
            }
            return score;
        }
    }

    public static class Coord {
        private final int x;
        private final int y;

        public Coord(final String coordString) {
            final String[] parts = coordString.split(",");
            this.x = Integer.parseInt(parts[0]);
            this.y = Integer.parseInt(parts[1]);
        }
    }
}

