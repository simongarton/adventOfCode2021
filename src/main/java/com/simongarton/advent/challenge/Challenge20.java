package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Challenge20 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Template 1";
    private static final String TITLE_2 = "Template 2";



    private String algorithm;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        this.algorithm = lines[0];
        List<String> image = new ArrayList<>();
        for (int i = 2; i < lines.length; i++) {
            image.add(lines[i]);
        }
//        this.printImage(image);
        for (int round = 0; round < 2; round++) {
            image = this.expandImage(image);
            this.printImage(image);
//            this.debugPattern(image, 3, 3);
            image = this.processImage(image);
            this.printImage(image);
        }
        final long result = this.countLit(image);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private void debugPattern(final List<String> image, final int row, final int col) {
        final String pattern = this.readPattern(image, row, col);
        final String binary = this.patternToBinary(pattern);
        final int encoded = Integer.parseInt(binary, 2);
        final String enhanced = this.algorithm.charAt(encoded) + "";
        System.out.println(encoded + " = " + enhanced + " for " + pattern);
    }

    private List<String> processImage(final List<String> image) {
        final int width = image.get(0).length();
        final int height = image.size();
        final List<String> processed = new ArrayList<>();
        final String blankLine = new String(new char[width]).replace("\0", ".");
        processed.add(blankLine);
        System.out.println("started " + processed.get(processed.size() - 1));

        for (int row = 1; row < height - 1; row++) {
            String newLine = ".";
            for (int col = 1; col < width - 1; col++) {
                final String pattern = this.readPattern(image, row, col);
                final String binary = this.patternToBinary(pattern);
                final int encoded = Integer.parseInt(binary, 2);
                final String enhanced = this.algorithm.charAt(encoded) + "";
                System.out.println(row + "," + col + " : " + encoded + " = " + enhanced + " for " + pattern + " (" + binary + ")");
                newLine += enhanced;
            }
            processed.add(newLine + ".");
            System.out.println("added " + processed.get(processed.size() - 1));
        }

        processed.add(blankLine);
        return processed;
    }

    private String patternToBinary(final String pattern) {
        String binary = pattern;
        binary = binary.replace(".", "0");
        binary = binary.replace("#", "1");
        return binary;
    }

    private String readPattern(final List<String> image, final int row, final int col) {
        String pattern = "";
        pattern += image.get(row - 1).charAt(col - 1);
        pattern += image.get(row - 1).charAt(col);
        pattern += image.get(row - 1).charAt(col + 1);
        pattern += image.get(row).charAt(col - 1);
        pattern += image.get(row).charAt(col);
        pattern += image.get(row).charAt(col + 1);
        pattern += image.get(row + 1).charAt(col - 1);
        pattern += image.get(row + 1).charAt(col);
        pattern += image.get(row + 1).charAt(col + 1);
        return pattern;
    }

    private List<String> expandImage(final List<String> image) {
        final int width = image.get(0).length();
        final String blankLine = new String(new char[width + 2]).replace("\0", ".");
        final List<String> expanded = new ArrayList<>();
        expanded.add(blankLine);
        for (final String line : image) {
            expanded.add("." + line + ".");
        }
        expanded.add(blankLine);
        return expanded;
    }

    private void printImage(final List<String> image) {
        for (final String line : image) {
            System.out.println(line);
        }
        System.out.println();
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
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }
}
