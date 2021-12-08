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

    private void run() throws IOException {
//        this.day1();
//        this.day2();
//        this.day3();
//        this.day4();
//        this.day5();
//        this.day6();
//        this.day7();
        this.day8();
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

}
