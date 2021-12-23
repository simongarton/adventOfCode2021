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
    void testToString() {
        // given
        final String line = "[[[[[9,8],1],2],3],4]";

        // when
        final Snailfish snailfish = new Snailfish(line);

        // then
        assertEquals(line, snailfish.toString());
    }

    @ParameterizedTest
    @MethodSource("provideStringsForExplode")
    void explode(final String line, final String expected) {
        // given
        final Snailfish snailfish = new Snailfish(line);

        // when
        final Snailfish exploded = Snailfish.explode(snailfish);

        // then
        assertNotNull(exploded);
        assertEquals(expected, exploded.toString());
    }

    @Test
    void add() {
        // given
        final Snailfish one = new Snailfish("[1,2]");
        final Snailfish two = new Snailfish("[[3,4],5]");

        // when
        final Snailfish three = one.add(two);

        // then
        assertEquals("[[1,2],[[3,4],5]]", three.toString());
    }

    @ParameterizedTest
    @MethodSource("provideStringsForFindNumberBackwards")
    void findNumberStartingAtIndexAndGoingBackwards(final String line, final int index, final int expected) {
        // given

        // when
        final Snailfish.StringReference stringReference = Snailfish.findNumberStartingAtIndexAndGoingBackwards(line, index);

        // then
        assertEquals(expected, stringReference.originalValue);
    }

    @ParameterizedTest
    @MethodSource("provideStringsForFindNumberForwards")
    void findNumberStartingAtIndexAndGoingForwards(final String line, final int index, final int expected) {
        // given

        // when
        final Snailfish.StringReference stringReference = Snailfish.findNumberStartingAtIndexAndGoingForwards(line, index);

        // then
        assertEquals(expected, stringReference.originalValue);
    }

    public static Stream<Arguments> provideStringsForFindNumberBackwards() {
        return Stream.of(
                Arguments.of("[[1,2],[3,4]]", 4, 2),
                Arguments.of("[[1,20],[3,4]]", 5, 20)
        );
    }

    public static Stream<Arguments> provideStringsForFindNumberForwards() {
        return Stream.of(
                Arguments.of("[[1,2],[3,4]]", 8, 3),
                Arguments.of("[[1,2],[30,4]]", 8, 30)
        );
    }

    public static Stream<Arguments> provideStringsForExplode() {
        return Stream.of(
//                Arguments.of("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]"),
                Arguments.of("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]")
//                Arguments.of("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]"),
//                Arguments.of("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
//                Arguments.of("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
        );
    }
}