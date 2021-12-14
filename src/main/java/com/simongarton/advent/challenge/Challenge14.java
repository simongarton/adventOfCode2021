package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Challenge14 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private List<Insertion> insertions;

    private static final String TITLE_1 = "Extended Polymerization 1";
    private static final String TITLE_2 = "Extended Polymerization 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge14() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final String template = lines[0];
        this.insertions = new ArrayList<>();
        this.loadInsertions(lines);
        String current = template;
//        System.out.println(0 + " : " + current);
        for (int step = 1; step <= 40; step++) {
            current = this.applyInsertions(current);
            System.out.println(step + " : " + current.length());
        }
        final long result = this.scorePolymer(current);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long scorePolymer(final String current) {
        final Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < current.length(); i++) {
            final String element = current.charAt(i) + "";
            map.put(element, map.getOrDefault(element, 0) + 1);
        }
        int min = Integer.MAX_VALUE;
        String minChar = "";
        int max = 0;
        String maxChar = "";
        for (final Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() < min) {
                min = entry.getValue();
                minChar = entry.getKey();
            }
            if (entry.getValue() > max) {
                max = entry.getValue();
                maxChar = entry.getKey();
            }
        }
        return map.get(maxChar) - map.get(minChar);
    }

    private String applyInsertions(final String current) {
        final List<Insertion> insertionsToApply = new ArrayList<>();
        for (final Insertion insertion : this.insertions) {
            for (int i = 0; i < current.length() - 1; i++) {
                final String match = current.substring(i, i + 2);
                if (match.equals(insertion.pattern)) {
                    final Insertion newInsertion = insertion.copy();
                    newInsertion.indexInTemplate = i + 1;
                    insertionsToApply.add(newInsertion);
//                    System.out.println("I will apply " + newInsertion.add + " between " + newInsertion.insertion + " at " + newInsertion.indexInTemplate);
                }
            }
        }
        return this.actuallyApplyInsertions(current, insertionsToApply);
    }

    private void updateRemainingInsertionsIfLater(List<Insertion> insertionsToApply, int i) {
        for (final Insertion insertion : insertionsToApply) {
            if (insertion.indexInTemplate >= i) {
                insertion.indexInTemplate++;
//                System.out.println("I have changed " + insertion.add + " between " + insertion.insertion + " to now be " + insertion.indexInTemplate);
            }
        }
    }

    private String actuallyApplyInsertions(final String current, final List<Insertion> insertionsToApply) {
        String inserted = current;
        for (final Insertion insertion : insertionsToApply) {
//            System.out.println("I am inserting " + insertion.add + " between " + insertion.insertion + " at " + insertion.indexInTemplate);
            inserted = inserted.substring(0, insertion.indexInTemplate) + insertion.add + inserted.substring(insertion.indexInTemplate);
            updateRemainingInsertionsIfLater(insertionsToApply, insertion.indexInTemplate);
        }
        return inserted;
    }

    private void loadInsertions(final String[] lines) {
        for (int index = 2; index < lines.length; index++) {
            this.insertions.add(new Insertion(lines[index]));
        }
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    public static final class Insertion {
        String left;
        String right;
        String pattern;
        String add;
        String insertion;
        int indexInTemplate;
        boolean apply;

        public Insertion(final String insertion) {
            this.insertion = insertion;
            final String[] parts = insertion.split(" -> ");
            this.pattern = parts[0];
            this.left = this.pattern.charAt(0) + "";
            this.right = this.pattern.charAt(1) + "";
            this.add = parts[1];
            this.indexInTemplate = 0;
            this.apply = false;
        }

        public Insertion copy() {
            return new Insertion(this.insertion);
        }
    }
}

