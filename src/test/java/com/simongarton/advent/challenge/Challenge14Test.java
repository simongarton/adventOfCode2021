package com.simongarton.advent.challenge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Challenge14Test {

    private Challenge14 sut;

    @BeforeEach
    void setup() {
        this.sut = new Challenge14();
    }

    @Test
    void applyInsertions() {
        // given
        final List<String> lines = new ArrayList<>();
        lines.add("NNN");
        lines.add("");
        lines.add("NN -> C");
        this.sut.loadInsertions(lines.toArray(new String[0]));
        final String template = lines.get(0);
        Map<String, Long> pairs = this.sut.buildPairs(template);
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        System.out.println("");

        // when
        pairs = this.sut.applyInsertions(pairs);

        // then
        for (final Map.Entry<String, Long> entry : pairs.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}