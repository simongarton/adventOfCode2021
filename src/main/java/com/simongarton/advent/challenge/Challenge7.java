package com.simongarton.advent.challenge;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Challenge7 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "The Treachery of Whales 1";
    private static final String TITLE_2 = "The Treachery of Whales 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge7() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final List<CrabBucket> crabBuckets = this.loadCrabsIntoBuckets(lines);
        final long result = this.bestPosition(crabBuckets);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private Long bestPosition(final List<CrabBucket> crabBuckets) {
        final Integer maxDistance = crabBuckets.stream().map(CrabBucket::getDistance).max(Integer::compare).orElse(null);
        final Map<Integer, Long> options = new HashMap<>();
        Long minimum = null;
        Integer bestPosition = null;
        for (int i = 0; i <= maxDistance; i++) {
            final long cost = this.moveCrabsTo(crabBuckets, i);
            options.put(i, cost);
            if ((minimum == null) || (minimum > cost)) {
                minimum = cost;
                bestPosition = i;
            }
        }
        this.debugCosts(options, minimum, bestPosition);
        return minimum;
    }

    private Long bestExpensivePosition(final List<CrabBucket> crabBuckets) {
        final Integer maxDistance = crabBuckets.stream().map(CrabBucket::getDistance).max(Integer::compare).orElse(null);
        final Map<Integer, Long> options = new HashMap<>();
        Long minimum = null;
        Integer bestPosition = null;
        for (int i = 0; i <= maxDistance; i++) {
            final long cost = this.moveCrabsExpensivelyTo(crabBuckets, i);
            options.put(i, cost);
            if ((minimum == null) || (minimum > cost)) {
                minimum = cost;
                bestPosition = i;
            }
        }
        this.debugCosts(options, minimum, bestPosition);
        return minimum;
    }

    private void debugCosts(final Map<Integer, Long> options, final Long minimum, final Integer bestPosition) {
        final List<Integer> positions = new ArrayList<>(options.keySet());
        Collections.sort(positions);
        for (final Integer position : positions) {
            System.out.println(position + " : " + options.get(position));
        }
        System.out.println("");
        System.out.println("minimum " + minimum + " at " + bestPosition);
    }

    private long moveCrabsTo(final List<CrabBucket> crabBuckets, final int position) {
        long cost = 0;
        for (final CrabBucket crabBucket : crabBuckets) {
            cost += (long) crabBucket.getCrabs() * (Math.abs(crabBucket.getDistance() - position));
        }
        return cost;
    }

    private long moveCrabsExpensivelyTo(final List<CrabBucket> crabBuckets, final int position) {
        long cost = 0;
        for (final CrabBucket crabBucket : crabBuckets) {
            cost += this.expensiveCost(crabBucket.getCrabs(), (Math.abs(crabBucket.getDistance() - position)));
        }
        return cost;
    }

    private long expensiveCost(final int crabs, final int distance) {
        long truecost = 0;
        int increment = 1;
        for (int i = 0; i < distance; i++) {
            truecost = truecost + increment;
            increment++;
        }
        return crabs * truecost;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final List<CrabBucket> crabBuckets = this.loadCrabsIntoBuckets(lines);
        final long result = this.bestExpensivePosition(crabBuckets);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private List<CrabBucket> loadCrabsIntoBuckets(final String[] lines) {
        final String[] crabs = lines[0].split(",");
        final Map<Integer, Integer> positions = new HashMap<>();
        for (final String crab : crabs) {
            final int distance = Integer.parseInt(crab);
            positions.put(distance, positions.getOrDefault(distance, 0) + 1);
        }
        final List<CrabBucket> crabBuckets = new ArrayList<>();
        for (final Map.Entry<Integer, Integer> entry : positions.entrySet()) {
            crabBuckets.add(new CrabBucket(entry.getKey(), entry.getValue()));
        }
        Collections.sort(crabBuckets, Comparator.comparing(CrabBucket::getDistance));
        this.debugCrabBuckets(crabBuckets);
        return crabBuckets;
    }

    private void debugCrabBuckets(final List<CrabBucket> crabBuckets) {
        for (final CrabBucket crabBucket : crabBuckets) {
            System.out.println(crabBucket.getDistance() + ":" + crabBucket.getCrabs());
        }
        System.out.println("");
    }

    @Data
    @AllArgsConstructor
    private static final class CrabBucket {
        private int distance;
        private int crabs;
    }
}
