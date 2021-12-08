package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class Challenge8 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Seven Segment Search 1";
    private static final String TITLE_2 = "Seven Segment Search 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge8() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = this.count1478(lines);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long count1478(final String[] lines) {
        long total = 0;
        for (final String line : lines) {
            total += this.count1478(line);
        }
        return total;
    }

    private long count1478(final String line) {
        long total = 0;
        final String cleanLine = line.replace("  ", " ");
        final String[] parts = cleanLine.split("\\|");
        final String[] output = parts[1].trim().split(" ");
        for (final String digit : output) {
            if (List.of(2, 3, 4, 7).contains(digit.length())) {
                total++;
            }
        }
        System.out.println(line + " " + total);
        return total;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = this.addOutputs(lines);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long addOutputs(final String[] lines) {
        long total = 0;
        for (final String line : lines) {
            total += this.addOutputs(line);
        }
        return total;
    }

    private long addOutputs(final String line) {
        final String cleanLine = line.replace("  ", " ");
        final String[] parts = cleanLine.split("\\|");
        final String[] outputs = parts[1].trim().split(" ");
        final Map<String, Integer> wiring = this.figureWiring(parts[0].trim().split(" "));
        final StringBuilder totalLine = new StringBuilder();
        for (final String output : outputs) {
            totalLine.append(this.decodeOutput(wiring, output));
        }
        return Long.parseLong(totalLine.toString());
    }

    private long decodeOutput(final Map<String, Integer> wiring, final String output) {
        long total = 0;
        for (final String digit : output.split(" ")) {
            total += this.mapToWiring(wiring, digit);
        }
        return total;
    }

    private long mapToWiring(final Map<String, Integer> wiring, final String digit) {
        for (final String key : wiring.keySet()) {
            if (!(key.length() == digit.length())) {
                continue;
            }
            if (this.sameLetters(key, digit)) {
                return wiring.get(key);
            }
        }
        throw new RuntimeException("Did not find " + digit + " in table");
    }

    private boolean sameLetters(final String key, final String digit) {
        final String sortedKey = this.sort(key);
        final String sortedDigit = this.sort(digit);
        return sortedKey.equalsIgnoreCase(sortedDigit);
    }

    private String sort(final String value) {
        final char[] tempArray = value.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    private Map<String, Integer> figureWiring(final String[] signals) {
        return this.figureWiringBruteForce(signals);
    }

    private Map<String, Integer> figureWiringBruteForce(final String[] signals) {
        // this maps the individual segments, clearText:encrypted
        final Map<String, String> wiringMapping = this.wiringMapping(signals);
        final Map<String, String> reverseWiringMapping = new HashMap<>();
        for (final Map.Entry<String, String> entry : wiringMapping.entrySet()) {
            reverseWiringMapping.put(entry.getValue(), entry.getKey());
        }
        // now I need to generate a list of codes that generate numbers - sorted. so "cf" would be 1,
        final Map<String, Integer> clearTextValues = this.buildClearTextValues();

        // then I need to take the signals, sort them in their encrypted form,
        final List<String> sortedEncrypted = Arrays.stream(signals).map(this::sort).collect(Collectors.toList());
        // translate them into a cleartext form to figure out the value, and then set up
        // the sorted table of sorted/encrypted : value
        final Map<String, Integer> encryptedValues = new HashMap<>();
        for (final String encrypted : sortedEncrypted) {
            final String clearText = this.decrypt(encrypted, reverseWiringMapping);
            final String sortedClearText = this.sort(clearText);
            final int value = clearTextValues.get(sortedClearText);
            encryptedValues.put(encrypted, value);
        }

        // and I will then loook up the sorted signals in this.
        return encryptedValues;
    }

    private String decrypt(final String encrypted, final Map<String, String> wiringMapping) {
        String clearText = "";
        for (int i = 0; i < encrypted.length(); i++) {
            clearText += wiringMapping.get(encrypted.charAt(i) + "");
        }
        return clearText;
    }

    private Map<String, Integer> buildClearTextValues() {
        final Map<String, Integer> map = new HashMap<>();
        map.put("abcefg", 0);
        map.put("cf", 1);
        map.put("acdeg", 2);
        map.put("acdfg", 3);
        map.put("bcdf", 4);
        map.put("abdfg", 5);
        map.put("abdefg", 6);
        map.put("acf", 7);
        map.put("abcdefg", 8);
        map.put("abcdfg", 9);
        return map;
    }

    private Map<String, String> wiringMapping(final String[] signals) {
        // this will give me a table of "x":"y" where x is the normal segment a-g (a top)
        // and y is how it's been wired up.

        final Map<String, String> wiringMapping = new HashMap<>();

        // To get a, I find the length 2 and length 3 : the extra one is a
        wiringMapping.put("a", this.findA(signals));

        // b is the only segment mentioned 6 times out of the 10 possibles
        wiringMapping.put("b", this.findFromCount(signals, 6));

        // c is one of two segments mentioned 8 times out of the 10 possibles, the other is a
        wiringMapping.put("c", this.findC(signals, 8, wiringMapping.get("a")));

        // d is mentioned 7 times AND IS in the unique signal that is 4 chars long
        wiringMapping.put("d", this.findDG(signals, 7, true));

        // e is the only segment mentioned 4 times out of the 10 possibles
        wiringMapping.put("e", this.findFromCount(signals, 4));

        // f is the only segment mentioned 9 times out of the 10 possibles
        wiringMapping.put("f", this.findFromCount(signals, 9));

        // g is mentioned 7 times BUT NOT in the unique signal that is 4 chars long
        wiringMapping.put("g", this.findDG(signals, 7, false));

        return wiringMapping;
    }

    private String findDG(final String[] signals, final int count, final boolean in4Count) {
        // TODO precalculate
        final Map<String, Integer> frequencyAnalysis = new HashMap<>();
        String count4 = "";
        for (final String signal : signals) {
            if (signal.length() == 4) {
                count4 = signal;
            }
            for (int i = 0; i < signal.length(); i++) {
                final String c = signal.charAt(i) + "";
                frequencyAnalysis.put(c, frequencyAnalysis.getOrDefault(c, 0) + 1);
            }
        }
        if (count4 == "") {
            throw new RuntimeException("Did not find count4 in DG");
        }
        final List<String> options = new ArrayList<>();
        for (final Map.Entry<String, Integer> entry : frequencyAnalysis.entrySet()) {
            if (entry.getValue().equals(count)) {
                options.add(entry.getKey());
            }
        }
        if (options.size() != 2) {
            throw new RuntimeException("Did not find dg options.");
        }
        if (count4.contains(options.get(0)) == in4Count) {
            return options.get(0);
        }
        if (count4.contains(options.get(1)) == in4Count) {
            return options.get(1);
        }
        throw new RuntimeException("Could not find DG looking at " + count4);
    }

    private String findC(final String[] signals, final int count, final String aWiring) {
        // TODO precalculate
        final Map<String, Integer> frequencyAnalysis = new HashMap<>();
        for (final String signal : signals) {
            for (int i = 0; i < signal.length(); i++) {
                final String c = signal.charAt(i) + "";
                frequencyAnalysis.put(c, frequencyAnalysis.getOrDefault(c, 0) + 1);
            }
        }
        final List<String> options = new ArrayList<>();
        for (final Map.Entry<String, Integer> entry : frequencyAnalysis.entrySet()) {
            if (entry.getValue().equals(count)) {
                options.add(entry.getKey());
            }
        }
        if (options.size() != 2) {
            throw new RuntimeException("Did not find c with a:" + aWiring);
        }
        if (options.get(0).equalsIgnoreCase(aWiring)) {
            return options.get(1);
        }
        if (options.get(1).equalsIgnoreCase(aWiring)) {
            return options.get(0);
        }
        throw new RuntimeException("Neither options for c was a:" + aWiring);
    }

    private String findFromCount(final String[] signals, final int count) {
        // TODO precalculate
        final Map<String, Integer> frequencyAnalysis = new HashMap<>();
        for (final String signal : signals) {
            for (int i = 0; i < signal.length(); i++) {
                final String c = signal.charAt(i) + "";
                frequencyAnalysis.put(c, frequencyAnalysis.getOrDefault(c, 0) + 1);
            }
        }
        for (final Map.Entry<String, Integer> entry : frequencyAnalysis.entrySet()) {
            if (entry.getValue().equals(count)) {
                return entry.getKey();
            }
        }
        throw new RuntimeException("Did not find " + count);
    }

    private String findA(final String[] signals) {
        final String two = Arrays.stream(signals).filter(s -> s.length() == 2).findFirst().orElseThrow(() -> new RuntimeException("Didn't find a 2 length string"));
        final String three = Arrays.stream(signals).filter(s -> s.length() == 3).findFirst().orElseThrow(() -> new RuntimeException("Didn't find a 2 length string"));
        for (int i = 0; i < three.length(); i++) {
            if (!(two.contains(three.charAt(i) + ""))) {
                return three.charAt(i) + "";
            }
        }
        throw new RuntimeException("find() failed");
    }

    private Map<String, Integer> figureWiringCheating(final String signals) {
        final Map<String, Integer> map = new HashMap<>();
        map.put("acedgfb", 8);
        map.put("cdfbe", 5);
        map.put("gcdfa", 2);
        map.put("fbcad", 3);
        map.put("dab", 7);
        map.put("cefabd", 9);
        map.put("cdfgeb", 6);
        map.put("eafb", 4);
        map.put("cagedb", 0);
        map.put("ab", 1);
        return map;
    }
}
