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

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final String template = lines[0];
        this.insertions = new ArrayList<>();
        this.loadInsertions(lines);
        String current = template;
        for (int step = 1; step <= 10; step++) {
            current = this.applyInsertions(current);
            System.out.println(step + " l " + current.length() + " s " + this.scorePolymer(current) + " c " + current);
        }
        final long result = this.scorePolymer(current);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final String template = lines[0];
        this.loadInsertions(lines);
        Map<String, Long> pairs = this.buildPairs(template);
        debugPairs("pairs at start",pairs);
        for (int step = 1; step <= 40; step++) {
            pairs = this.applyInsertions(pairs);
            debugPairs("pairs",pairs);
            System.out.println(step + " s " + this.scorePolymer(pairs, template));
        }
        final long result = this.scorePolymer(pairs, template);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private void debugPairs(String s, Map<String, Long> pairs) {
        String line = "";
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            line += entry.getKey() + "=" + entry.getValue() + ", ";
        }
        System.out.println(s + " " + line);
    }

    protected Map<String, Long> applyInsertions(final Map<String, Long> pairs) {
        final Map<String, Long> newPairs = new HashMap<>();
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            newPairs.put(entry.getKey(), entry.getValue());
        }
        for (final Insertion insertion : this.insertions) {
            if (pairs.containsKey(insertion.pattern)) {
                // decrement the existing pair by how many we have of these.
                newPairs.put(insertion.pattern, newPairs.get(insertion.pattern) - pairs.get(insertion.pattern));
                // figure out the new ones
                final String new1 = insertion.left + insertion.add;
                final String new2 = insertion.add + insertion.right;
                newPairs.put(new1, newPairs.getOrDefault(new1, 0L) + pairs.get(insertion.pattern));
                newPairs.put(new2, newPairs.getOrDefault(new2, 0L) + pairs.get(insertion.pattern));
            }
        }
        final Map<String, Long> positivePairs = new HashMap<>();
        for (final Map.Entry<String, Long> entry : newPairs.entrySet()) {
            if (entry.getValue() > 0) {
                positivePairs.put(entry.getKey(), entry.getValue());
            }
        }
        return positivePairs;
    }

    private long scorePolymer(final String current) {
        final Map<String, Long> map = new HashMap<>();
        for (int i = 0; i < current.length(); i++) {
            final String element = current.charAt(i) + "";
            map.put(element, map.getOrDefault(element, 0L) + 1);
        }
        return this.scoreMap(map);
    }

    protected long scoreMap(final Map<String, Long> map, String template) {
        debugPairs("scoreMapTemplate", map);
        // now halve them all
        for (final Map.Entry<String, Long> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue()/2);
        }
        // and unhalf the first and last
        String first = template.substring(0, 1);
        map.put(first, map.get(first) + 1);
        String last = template.substring(template.length()-1, template.length());
        map.put(last, map.get(last) + 1);
        debugPairs("scoreMapTemplate2", map);
        long min = Long.MAX_VALUE;
        String minChar = "";
        long max = 0;
        String maxChar = "";
        for (final Map.Entry<String, Long> entry : map.entrySet()) {
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

    protected long scoreMap(final Map<String, Long> map) {
        debugPairs("scoreMap", map);
        long min = Long.MAX_VALUE;
        String minChar = "";
        long max = 0;
        String maxChar = "";
        for (final Map.Entry<String, Long> entry : map.entrySet()) {
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

    private long scorePolymer(final Map<String, Long> pairs, String template) {
        // it breaks down here. I have pairs, and I know how many of each pair I have
        // but the pairs overlap. So if I have a string ABC and my pairs are AB and BC, I cannot count B twice.
        // which is why I'm getting about double the result.
        // but I know the first and last letters. I should halve every count, and then add 1 to each of those.
        final Map<String, Long> map = new HashMap<>();
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            String element = entry.getKey().charAt(0) + "";
            map.put(element, map.getOrDefault(element, 0L) + entry.getValue());
            element = entry.getKey().charAt(1) + "";
            map.put(element, map.getOrDefault(element, 0L) + entry.getValue());
        }
        return this.scoreMap(map, template);
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

    private void updateRemainingInsertionsIfLater(final List<Insertion> insertionsToApply, final int i) {
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
            this.updateRemainingInsertionsIfLater(insertionsToApply, insertion.indexInTemplate);
        }
        return inserted;
    }

    protected void loadInsertions(final String[] lines) {
        this.insertions = new ArrayList<>();
        for (int index = 2; index < lines.length; index++) {
            this.insertions.add(new Insertion(lines[index]));
        }
    }

    protected Map<String, Long> buildPairs(final String template) {
        final Map<String, Long> pairs = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            final String key = template.substring(i, i + 2);
            pairs.put(key, pairs.getOrDefault(key, 0L) + 1);
        }
        return pairs;
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

