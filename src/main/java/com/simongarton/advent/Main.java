package com.simongarton.advent;

import com.simongarton.advent.challenge.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(final String[] args) throws IOException {

        final Main main = new Main();
        main.run();
    }

    private void runAll() throws IOException {
        this.day1();
        this.day2();
        this.day3();
        this.day4();
        this.day5();
        this.day6();
        this.day7();
        this.day8();
        this.day10();
        this.day11();
        this.day12();
        this.day13();
        this.day14();
        this.day15();
        this.day16();
        this.day17();
        this.day18();
        this.day19();
        this.day20();
        this.day21();
        this.day22();
        this.day23();
        this.day24();
        this.day25();
    }

    private void run() throws IOException {
        this.day23();
    }

    private void day1() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day1.txt")).toArray(new String[0]);
        new Challenge1().run(lines);
    }

    private void day2() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day2.txt")).toArray(new String[0]);
        new Challenge2().run(lines);
    }

    private void day3() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day3.txt")).toArray(new String[0]);
        new Challenge3().run(lines);
    }

    private void day4() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day4.txt")).toArray(new String[0]);
        new Challenge4().run(lines);
    }

    private void day5() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day5.txt")).toArray(new String[0]);
        new Challenge5().run(lines);
    }

    private void day6() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day6.txt")).toArray(new String[0]);
        new Challenge6().run(lines);
    }

    private void day7() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day7.txt")).toArray(new String[0]);
        new Challenge7().run(lines);
    }

    private void day8() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day8.txt")).toArray(new String[0]);
        new Challenge8().run(lines);
    }

    private void day10() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day10.txt")).toArray(new String[0]);
        new Challenge10().run(lines);
    }

    private void day11() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day11.txt")).toArray(new String[0]);
        new Challenge11().run(lines);
    }

    private void day12() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day12.txt")).toArray(new String[0]);
        new Challenge12().run(lines);
    }

    private void day13() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day13.txt")).toArray(new String[0]);
        new Challenge13().run(lines);
    }

    private void day14() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day14.txt")).toArray(new String[0]);
        new Challenge14().run(lines);
    }

    private void day15() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day15.txt")).toArray(new String[0]);
        new Challenge15().run(lines);
    }

    private void day16() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day16.txt")).toArray(new String[0]);
        new Challenge16().run(lines);
    }

    private void day17() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day17.txt")).toArray(new String[0]);
        new Challenge17().run(lines);
    }

    private void day18() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day18.txt")).toArray(new String[0]);
        new Challenge18().run(lines);
    }

    private void day19() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day19-sample.txt")).toArray(new String[0]);
        new Challenge19().run(lines);
    }

    private void day20() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day20.txt")).toArray(new String[0]);
        new Challenge20().run(lines);
    }

    private void day21() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day21.txt")).toArray(new String[0]);
        new Challenge21().run(lines);
    }

    private void day22() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day22.txt")).toArray(new String[0]);
        new Challenge22().run(lines);
    }

    private void day23() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day23-sample.txt")).toArray(new String[0]);
        new Challenge23().run(lines);
    }

    private void day24() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day24.txt")).toArray(new String[0]);
        new Challenge24().run(lines);
    }

    private void day25() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day25.txt")).toArray(new String[0]);
        new Challenge25().run(lines);
    }
}
