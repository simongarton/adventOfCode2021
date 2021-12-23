package com.simongarton.advent.challenge;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SnailfishTest {

    @Test
    void getLevel() {
        // given
        final String line = "[[3,[1,2]],3]";
        final Snailfish snailfish = new Snailfish(line, null);

        // when
        final int level = snailfish.getLevel();

        // then
        snailfish.listPairs();
    }

    @ParameterizedTest
    @MethodSource("provideStringsForExplode")
    void explode(final String line, final String expected) {
        // given
        final Snailfish snailfish = new Snailfish(line, null);

        // when
        snailfish.explode();

        // then
        assertNotNull(snailfish);
        assertEquals(expected, snailfish.toString());
    }

    public static Stream<Arguments> provideStringsForExplode() {
        return Stream.of(
                Arguments.of("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"),
                Arguments.of("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]"),
                Arguments.of("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"),
                Arguments.of("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
                Arguments.of("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]"),
                Arguments.of("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]", "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
        );
    }

    @ParameterizedTest
    @MethodSource("provideStringsForSplit")
    void split(final String line, final String expected) {
        // given
        final Snailfish snailfish = new Snailfish(line, null);

        // when
        snailfish.split();

        // then
        assertNotNull(snailfish);
        assertEquals(expected, snailfish.toString());
    }

    public static Stream<Arguments> provideStringsForSplit() {
        return Stream.of(
                Arguments.of("[10,0]", "[[5,5],0]"),
                Arguments.of("[11,0]", "[[5,6],0]"),
                Arguments.of("[[[[0,7],4],[15,[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"),
                Arguments.of("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]", "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")
        );
    }
}