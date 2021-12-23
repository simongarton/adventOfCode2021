package com.simongarton.advent.challenge;

import com.google.gson.Gson;

public final class Snailfish {
    private static final Gson gson = new Gson();

    private Snailfish left;
    private Snailfish right;
    private Integer value;

    public Snailfish() {
    }

    public Snailfish(final String line) {
        final int index = this.findMiddleComma(line);
        if (index == -1) {
            this.value = Integer.parseInt(line);
            return;
        }
        final String leftPart = line.substring(1, index);
        final String rightPart = line.substring(index + 1, line.length() - 1);
//            System.out.println(leftPart + " | " + rightPart);
        this.left = new Snailfish(leftPart);
        this.right = new Snailfish(rightPart);
    }

    @Override
    public String toString() {
        if (this.value != null) {
            return this.value + "";
        }
        return "[" + this.left.toString() + "," + this.right.toString() + "]";
        //return gson.toJson(this);
    }

    private int findMiddleComma(final String line) {
        int brackets = 0;
        for (int index = 0; index < line.length(); index++) {
            final String c = line.charAt(index) + "";
            if (c.equalsIgnoreCase("[")) {
                brackets++;
                continue;
            }
            if (c.equalsIgnoreCase("]")) {
                brackets--;
                continue;
            }
            if (c.equalsIgnoreCase(",")) {
                if (brackets == 1) {
                    return index;
                }
            }
        }
        return -1;
    }

    private Snailfish add() {
        final String leftLine = gson.toJson(this.left);
        final String rightLine = gson.toJson(this.right);
        final String combinedLine = "{\"left\":" + leftLine + ",\"right\":" + rightLine + "}";
        return gson.fromJson(combinedLine, Snailfish.class);
    }

//    private Snailfish reduce() {
//        int actions = Integer.MAX_VALUE;
//        while (actions > 0) {
//            actions = 0;
//            if (this.explode()) {
//                actions++;
//                continue;
//            }
//            if (this.split()) {
//                actions++;
//                continue;
//            }
//        }
//        return this;
//    }

    public static Snailfish split(final Snailfish original) {
        return null;
    }

    public static Snailfish explode(final Snailfish original) {
        final String line = original.toString();
        int currentNesting = 0;
        for (int index = 0; index < line.length() - 1; index++) {
            if (line.charAt(index) == '[') {
                currentNesting++;
            }
            if (line.charAt(index) == ']') {
                currentNesting--;
            }
            // this will find a pair with the opening [ at index
            final String pair = getPairAtIndex(line, index);
            if (pair == null) {
                continue;
            }
            if (currentNesting == 4) {
                System.out.println("Found pair '" + pair + "' at index " + index + " with nesting " + currentNesting);
                return explodeAt(original, index, pair);
            }
        }
        return null;
    }

    private static Snailfish explodeAt(final Snailfish original, final int explodeAt, final String pair) {
        final String line = original.toString();
        final String internals = pair.substring(1, pair.length() - 1);
        // a pair might contain pairs, e.g. [4, [3,2]]
        // I think I might give up !
        final String[] pairParts = internals.split(",");
        // now I am looking for a number before the line index explodeAt. The
        // problem is that is might be more than 1 digit, so I can't check individual characters.
        // So I need to track left looking for a number, and then continue tracking left until I find a comma
        StringReference explodeLeft = null;
        // explodeAt - 2 as explodeAt is the opening [ of the pair, and it will be preceded by a comma
        for (int index = explodeAt - 2; index > 0; index--) {
            if (isNumber(line.charAt(index) + "")) {
                explodeLeft = findNumberStartingAtIndexAndGoingBackwards(line, index);
                explodeLeft.valueToAdd = Integer.parseInt(pairParts[0]);
                break;
            }
        }
        StringReference explodeRight = null;
        // start looking at the end of the current pair
        final int rightStart = explodeAt + pair.length();
        for (int index = rightStart; index < line.length() - 1; index++) {
            if (isNumber(line.charAt(index) + "")) {
                explodeRight = findNumberStartingAtIndexAndGoingForwards(line, index);
                explodeRight.valueToAdd = Integer.parseInt(pairParts[1]);
                break;
            }
        }
        return new Snailfish(assembleExplodedLine(line, explodeAt, pair, explodeLeft, explodeRight));
    }

    protected static String assembleExplodedLine(final String line,
                                                 final int explodeAt,
                                                 final String pair,
                                                 final StringReference explodeLeft,
                                                 final StringReference explodeRight) {

        // start [[[[[9,8],1],2],3],4]
        // end   [[[[0,9],2],3],4]
        //       012345678901234567890
        String finalLine = "";
        if (explodeLeft == null) {
            finalLine += line.substring(0, explodeAt);
        } else {
            finalLine += line.substring(0, explodeLeft.start);
            finalLine += String.valueOf(explodeLeft.originalValue + explodeLeft.valueToAdd);
            finalLine += line.substring(0 + explodeLeft.start + explodeLeft.length, explodeAt);
        }
        finalLine += "0";
        if (explodeRight == null) {
            finalLine += line.substring(explodeAt + pair.length());
        } else {
            finalLine += line.substring(explodeAt + pair.length(), explodeRight.start);
            finalLine += String.valueOf(explodeRight.originalValue + explodeRight.valueToAdd);
            finalLine += line.substring(explodeRight.start + explodeRight.length);
        }
        return finalLine;
    }

    protected static StringReference findNumberStartingAtIndexAndGoingBackwards(final String line, final int indexOfFirstDigitFound) {
        int start = indexOfFirstDigitFound;
        while (isNumber(line.charAt(start) + "")) {
            start--;
        }
        start++;
        final StringReference stringReference = new StringReference();
        stringReference.start = start;
        stringReference.length = indexOfFirstDigitFound - start;
        stringReference.substring = line.substring(start, indexOfFirstDigitFound + 1);
        stringReference.originalValue = Integer.parseInt(stringReference.substring);
        return stringReference;
    }

    protected static StringReference findNumberStartingAtIndexAndGoingForwards(final String line, final int indexOfFirstDigitFound) {
        int end = indexOfFirstDigitFound;
        while (isNumber(line.charAt(end) + "")) {
            end++;
        }
        final StringReference stringReference = new StringReference();
        stringReference.start = indexOfFirstDigitFound;
        stringReference.length = end - indexOfFirstDigitFound;
        stringReference.substring = line.substring(indexOfFirstDigitFound, end);
        stringReference.originalValue = Integer.parseInt(stringReference.substring);
        return stringReference;
    }

    private static boolean isNumber(final String character) {
        try {
            Integer.parseInt(character);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private static String getPairAtIndex(final String line, final int index) {
        if (line.charAt(index) != '[') {
            return null;
        }
        if (line.charAt(index + 1) == '[') {
            return null;
        }
        int pairsOpened = 0;
//        for (int i = 0; i < index; i++) {
//            if (line.charAt(index) != '[') {
//                pairsOpened++;
//            }
//            if (line.charAt(index + 1) == '[') {
//                pairsOpened--;
//            }
//        }
        for (int i = index; i < line.length(); i++) {
            if (line.charAt(i) == '[') {
                pairsOpened++;
            }
            if (line.charAt(i) == ']') {
                pairsOpened--;
                if (pairsOpened == 0) {
                    return line.substring(index, i + 1);
                }
            }
        }
        throw new RuntimeException("Can't find close pair.");
    }

    public Snailfish add(final Snailfish other) {
        final Snailfish snailfish = new Snailfish();
        snailfish.left = this;
        snailfish.right = other;
        return snailfish;
    }

    public static final class StringReference {
        int start;
        int length;
        String substring;

        int originalValue;
        int valueToAdd;
    }
}