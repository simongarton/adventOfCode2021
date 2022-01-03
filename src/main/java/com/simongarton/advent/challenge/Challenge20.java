package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Challenge20 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Trench Map 1";
    private static final String TITLE_2 = "Trench Map 2";

    private String algorithm;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = this.processImageCommon(lines, 2);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long processImageCommon(final String[] lines, final int iterations) {
        this.algorithm = lines[0];
        List<String> image = new ArrayList<>();
        for (int i = 2; i < lines.length; i++) {
            image.add(lines[i]);
        }
        image = this.expandImage(image, 60);
        for (int step = 1; step <= iterations; step++) {
            image = this.processImage(image, step);
        }
        return this.countLit(image);
    }

    private void debugPattern(final List<String> image, final int row, final int col, final int step) {
        final String pattern = this.readPattern(image, row, col, step);
        final String binary = this.patternToBinary(pattern);
        final int encoded = Integer.parseInt(binary, 2);
        final String enhanced = this.algorithm.charAt(encoded) + "";
        System.out.println(encoded + " = " + enhanced + " for " + pattern);
    }

    private List<String> processImage(final List<String> image, final int step) {
        final int width = image.get(0).length();
        final int height = image.size();
        final List<String> processed = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            final StringBuilder newLine = new StringBuilder();
            for (int col = 0; col < width; col++) {
                final String pattern = this.readPattern(image, row, col, step);
                final String binary = this.patternToBinary(pattern);
                final int encoded = Integer.parseInt(binary, 2);
                final String enhanced = this.algorithm.charAt(encoded) + "";
                newLine.append(enhanced);
            }
            processed.add(newLine + "");
        }

        return processed;
    }

    private String patternToBinary(final String pattern) {
        String binary = pattern;
        binary = binary.replace(".", "0");
        binary = binary.replace("#", "1");
        return binary;
    }

    private String readPattern(final List<String> image, final int row, final int col, final int step) {
        String pattern = "";
        pattern += this.infinitePattern(image, row - 1, col - 1, step);
        pattern += this.infinitePattern(image, row - 1, col, step);
        pattern += this.infinitePattern(image, row - 1, col + 1, step);
        pattern += this.infinitePattern(image, row, col - 1, step);
        pattern += this.infinitePattern(image, row, col, step);
        pattern += this.infinitePattern(image, row, col + 1, step);
        pattern += this.infinitePattern(image, row + 1, col - 1, step);
        pattern += this.infinitePattern(image, row + 1, col, step);
        pattern += this.infinitePattern(image, row + 1, col + 1, step);
        return pattern;
    }

    private String infinitePattern(final List<String> image, final int row, final int col, final int step) {
        final int width = image.get(0).length();
        final int height = image.size();
        final String infinite = step % 2 == 1 ? "." : "#";
        if ((row < 0) || row >= width) {
            return infinite;
        }
        if ((col < 0) || col >= height) {
            return infinite;
        }
        return image.get(row).charAt(col) + "";
    }

    private List<String> expandImage(final List<String> image, final int border) {
        final int width = image.get(0).length();
        final String blankLine = new String(new char[width + (2 * border)]).replace("\0", ".");
        final String sidePad = new String(new char[border]).replace("\0", ".");
        final List<String> expanded = new ArrayList<>();
        for (int i = 0; i < border; i++) {
            expanded.add(blankLine);
        }
        for (final String line : image) {
            expanded.add(sidePad + line + sidePad);
        }
        for (int i = 0; i < border; i++) {
            expanded.add(blankLine);
        }
        return expanded;
    }

    private void printImage(final List<String> image) {
        int index = 0;
        for (final String line : image) {
            System.out.println(this.padTo(index++ + "", 3) + " " + line);
        }
        System.out.println();
    }

    private String padTo(final String s, final int size) {
        if (s.length() > size) {
            return new String(new char[size]).replace("\0", "*");
        }
        return new String(new char[size - s.length()]).replace("\0", " ") + s;
    }

    private long countLit(final List<String> image) {
        long lit = 0;
        for (final String line : image) {
            lit += line.chars().filter(ch -> ch == '#').count();
        }
        return lit;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = this.processImageCommon(lines, 50);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }
}
