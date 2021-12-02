package com.simongarton.advent;

import com.simongarton.advent.challenge.Challenge1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(final String[] args) throws IOException {

        final Main main = new Main();
        main.run();
    }

    private void run() throws IOException {
        final String[] lines = Files.readAllLines(Paths.get("data/day1.txt")).toArray(new String[0]);
        new Challenge1().run(lines);
    }
}
