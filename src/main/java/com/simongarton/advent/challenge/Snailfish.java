package com.simongarton.advent.challenge;

import java.util.ArrayList;
import java.util.List;

public final class Snailfish {

    private Snailfish parent;
    private Snailfish left;
    private Snailfish right;
    private Integer value;

    public Snailfish(final String part, final Snailfish parent) {
        this.parent = parent;
        final int index = this.findMiddleComma(part);
        if (index == -1) {
            if (!part.equalsIgnoreCase("")) {
                this.value = Integer.parseInt(part);
            }
            return;
        }
        final String leftPart = part.substring(1, index);
        final String rightPart = part.substring(index + 1, part.length() - 1);
        this.left = new Snailfish(leftPart, this);
        this.right = new Snailfish(rightPart, this);
    }

    public int getLevel() {
        if (this.parent == null) {
            return 0;
        }
        return 1 + this.parent.getLevel();
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
        left.parent = add;
        add.right = right;
        right.parent = add;
        return explodeAndSplitUntilQuiet(add);
    }

    private static Snailfish explodeAndSplitUntilQuiet(final Snailfish add) {
        boolean changesMade = true;
        while (changesMade) {
            changesMade = false;
            if (add.explode()) {
                changesMade = true;
                continue;
            }
            if (add.split()) {
                changesMade = true;
            }
        }
        return add;
    }

    boolean split() {
        final List<Snailfish> snailfishList = this.getSnailfishList();
        for (int i = 0; i < snailfishList.size(); i++) {
            final Snailfish snailfish = snailfishList.get(i);
            if (snailfish.value == null) {
                continue;
            }
            if (snailfish.value < 10) {
                continue;
            }
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

        final Snailfish lefty = new Snailfish("", snailfish);
        lefty.value = (int) leftValue;
        lefty.parent = snailfish;
        snailfish.left = lefty;

        final Snailfish righty = new Snailfish("", snailfish);
        righty.value = (int) rightValue;
        righty.parent = snailfish;
        snailfish.right = righty;
    }

    boolean explode() {
        final List<Snailfish> snailfishList = this.getSnailfishList();
        for (int i = 0; i < snailfishList.size(); i++) {
            final Snailfish snailfish = snailfishList.get(i);
            if (snailfish.getLevel() == 5) {
                this.explodeThisSnailfish(snailfishList, i);
                return true;
            }
        }
        return false;
    }

    private void explodeThisSnailfish(final List<Snailfish> snailfishList, final int i) {
        final Snailfish snailfish = snailfishList.get(i);
        final int leftValue = snailfish.parent.left.value;
        int goingLeft = i - 1;
        while (goingLeft > 0) {
            final Snailfish lefty = snailfishList.get(goingLeft);
            if (lefty.value != null) {
                lefty.value += leftValue;
                break;
            }
            goingLeft--;
        }

        final int rightValue = snailfish.parent.right.value;
        int goingRight = i + 2;
        while (goingRight < snailfishList.size()) {
            final Snailfish righty = snailfishList.get(goingRight);
            if (righty.value != null) {
                righty.value += rightValue;
                break;
            }
            goingRight++;
        }

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
            final Snailfish snailfish = snailfishList.get(i);
            if (snailfish.parent != null) {
                System.out.println(this.padTo("" + i, 4) + ":"
                        + snailfish
                        + " (level "
                        + snailfish.getLevel()
                        + " parent "
                        + snailfish.parent
                        + ":"
                        + snailfish.parent.getLevel()
                        + ")");
            } else {
                System.out.println(this.padTo("" + i, 4) + ":"
                        + snailfish
                        + " (level "
                        + snailfish.getLevel()
                        + ")");
            }
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

    public long getMagnitude() {
        if (this.value != null) {
            return this.value;
        }
        return 3 * this.left.getMagnitude() + 2 * this.right.getMagnitude();
    }
}