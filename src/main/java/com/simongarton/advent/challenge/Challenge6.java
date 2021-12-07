package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Challenge6 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Lanternfish 1";
    private static final String TITLE_2 = "Lanternfish 2";

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    public Challenge6() {
    }

    private long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long[] buckets = new long[9];
        this.loadFishIntoBuckets(buckets, lines);
        final long result = this.breedFish(buckets, 80);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long[] buckets = new long[9];
        this.loadFishIntoBuckets(buckets, lines);
        final long result = this.breedFish(buckets, 256);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    private long breedFish(final long[] buckets, final int days) {
        for (int day = 0; day < days; day++) {
            final long breeders = buckets[0];
            for (int i = 0; i < 8; i++) {
                buckets[i] = buckets[i + 1];
            }
            buckets[6] = buckets[6] + breeders;
            buckets[8] = breeders;
        }
        long total = 0;
        for (int i = 0; i < 9; i++) {
            total += buckets[i];
        }
        return total;
    }

    private void loadFishIntoBuckets(final long[] buckets, final String[] lines) {
        final String[] ages = lines[0].split(",");
        for (final String age : ages) {
            final int fish = Integer.parseInt(age);
            buckets[fish] = buckets[fish] + 1;
        }
    }
}
