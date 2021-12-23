package com.simongarton.advent.challenge;

import java.util.ArrayList;
import java.util.List;

public final class Snailfish {

    private final String source;
    private final Snailfish parent;
    private Snailfish left;
    private Snailfish right;
    private Integer value;
    private final int level;

    public Snailfish(final String part, final Snailfish parent) {
        this.parent = parent;
        this.level = parent == null ? 0 : parent.level + 1;
        this.source = part;
        final int index = this.findMiddleComma(part);
        if (index == -1) {
            this.value = Integer.parseInt(part);
            return;
        }
        final String leftPart = part.substring(1, index);
        final String rightPart = part.substring(index + 1, part.length() - 1);
        this.left = new Snailfish(leftPart, this);
        this.right = new Snailfish(rightPart, this);
    }

    public Snailfish(final Snailfish clone) {
        this.source = clone.source;
        this.value = clone.value;
        this.left = clone.left;
        this.right = clone.right;
        this.parent = clone.parent;
        this.level = clone.level;
    }

    @Override
    public String toString() {
        if (this.value != null) {
            return this.value + "";
        }
        return "[" + this.left.toString() + "," + this.right.toString() + "]";
    }

    public static Snailfish add(final Snailfish left, final Snailfish right) {
        final Snailfish add = new Snailfish("", null);
        add.left = left;
        add.right = right;
        final Snailfish result = explodeAndSplitUntilQuiet(add);
        return result;
    }

    private static Snailfish explodeAndSplitUntilQuiet(final Snailfish add) {
        final Snailfish changeable = new Snailfish(add);
        boolean changesMade = false;
        while (!changesMade) {
            changesMade = false;
            if (add.explode()) {
                changesMade = true;
                continue;
            }
            if (add.split()) {
                changesMade = true;
            }
        }
        return changeable;
    }

    boolean split() {
        final List<Snailfish> snailfishList = this.getSnailfishList();
        this.listPairs();
        for (int i = 0; i < snailfishList.size(); i++) {
            final Snailfish snailfish = snailfishList.get(i);
            if (snailfish.value == null) {
                continue;
            }
            if (snailfish.value < 10) {
                continue;
            }
            System.out.println("splitting index " + i);
            this.splitThisSnailfish(snailfishList, i);
            return true;
        }
        return false;
    }

    private void splitThisSnailfish(final List<Snailfish> snailfishList, final int i) {
        final Snailfish snailfish = snailfishList.get(i);
        final long leftValue = Math.round(Math.floor(snailfish.value / 2.0));
        final long rightValue = Math.round(Math.ceil(snailfish.value / 2.0));
        snailfish.value = null;
        final Snailfish lefty = new Snailfish(snailfish);
        lefty.value = (int) leftValue;
        snailfish.left = lefty;
        final Snailfish righty = new Snailfish(snailfish);
        righty.value = (int) rightValue;
        snailfish.right = righty;
    }

    boolean explode() {
        final List<Snailfish> snailfishList = this.getSnailfishList();
        this.listPairs();
        for (int i = 0; i < snailfishList.size(); i++) {
            final Snailfish snailfish = snailfishList.get(i);
            if (snailfish.level == 5) {
                System.out.println("exploding index " + i);
                this.explodeThisSnailfish(snailfishList, i);
                return true;
            }
        }
        return false;
    }

    private void explodeThisSnailfish(final List<Snailfish> snailfishList, final int i) {
        final Snailfish snailfish = snailfishList.get(i);
        // first we track back, adding the left value to the first regular number to the left
        // of where we are at index i
        final int leftValue = snailfish.parent.left.value;
        int goingLeft = i - 1;
        while (goingLeft > 0) {
            final Snailfish lefty = snailfishList.get(goingLeft);
            System.out.println("looking at " + lefty + " at goingLeft " + goingLeft);
            if (lefty.value != null) {
                lefty.value += leftValue;
                break;
            }
            goingLeft--;
        }

        // then we add the right value to the first regular number to the right of where we are at index i;
        final int rightValue = snailfish.parent.right.value;
        int goingRight = i + snailfish.source.length() + 1;
        while (goingRight < snailfishList.size()) {
            final Snailfish righty = snailfishList.get(goingRight);
            System.out.println("looking at " + righty + " at goingRight " + goingRight);
            if (righty.value != null) {
                righty.value += rightValue;
                break;
            }
            goingRight++;
        }

        // and finally we "explode" this pair - the pair, which is a Snailfish with two children both with single values
        // becomes just a value. It will be the parent - and I need to delete both of the children.
        final Snailfish parent = snailfish.parent;
        parent.value = 0;
        parent.left = null;
        parent.right = null;
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

    public void drawTree() {
        System.out.println(this);
        this.drawTree(this, 0);
        System.out.println();
    }

    private void drawTree(final Snailfish snailfish, final int i) {
        if (snailfish.left != null) {
            System.out.println(this.indent(snailfish.left.toString(), i * 2));
            this.drawTree(snailfish.left, i + 1);
        }
        if (snailfish.right != null) {
            System.out.println(this.indent(snailfish.right.toString(), i * 2));
            this.drawTree(snailfish.right, i + 1);
        }
    }

    private String indent(final String s, final int size) {
        return new String(new char[size]).replace("\0", ".") + s;
    }

    private String padTo(final String s, final int size) {
        if (s.length() > size) {
            return new String(new char[size]).replace("\0", "*");
        }
        return new String(new char[size - s.length()]).replace("\0", " ") + s;
    }

    public void listPairs() {
        System.out.println(this);
        final List<Snailfish> snailfishList = this.getSnailfishList();
        for (int i = 0; i < snailfishList.size(); i++) {
            System.out.println(this.padTo("" + i, 4) + ":" + snailfishList.get(i) + " (level " + snailfishList.get(i).level + ")");
        }
        System.out.println();
    }

    public List<Snailfish> getSnailfishList() {
        final List<Snailfish> snailfishList = new ArrayList<>();
        this.addSnailfish(0, this, snailfishList);
        return snailfishList;
    }

    private void addSnailfish(final int index, final Snailfish snailfish, final List<Snailfish> snailfishList) {
        snailfishList.add(snailfish);
        if (snailfish.left != null) {
            this.addSnailfish(index + 1, snailfish.left, snailfishList);
        }
        if (snailfish.right != null) {
            this.addSnailfish(index + 1, snailfish.right, snailfishList);
        }
    }
}