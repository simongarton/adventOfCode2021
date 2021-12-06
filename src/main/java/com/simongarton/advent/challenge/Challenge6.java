package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private int part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        List<Fish> fishes = result(lines, 80);
        final int score = fishes.size();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                score,
                System.currentTimeMillis() - start));
        return score;
    }

    private List<Fish> result(final String[] lines, int days) {
        final List<Fish> fishes = this.loadFish(lines);
        for (int day = 0; day < days; day++) {
            final List<Fish> babies = new ArrayList<>();
            for (final Fish fish : fishes) {
                if (fish.live() == Action.BREED) {
                    babies.add(new Fish(8));
                }
            }
            fishes.addAll(babies);
            System.out.println(day + 1 + " = " + fishes .size()) ; //+ " : " + fishes.stream().map(f -> String.valueOf(f.age)).collect(Collectors.joining(",")));
        }
        return fishes;
    }

    private List<Fish> loadFish(final String[] lines) {
        final List<Fish> fish = new ArrayList<>();
        final String[] ages = lines[0].split(",");
        for (final String age : ages) {
            fish.add(new Fish(Integer.parseInt(age)));
        }
        return fish;
    }

    private int part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        List<Fish> fishes = result(lines, 256);
        final int score = fishes.size();
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                score,
                System.currentTimeMillis() - start));
        return score;
    }

    private static final class Fish {
        int age;

        public Fish(final int age) {
            this.age = age;
        }

        public Action live() {
            if (--this.age < 0) {
                this.age = 6;
                return Action.BREED;
            }
            return Action.LIVE;
        }
    }

    private enum Action {
        LIVE,
        BREED
    }
}
