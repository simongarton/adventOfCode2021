package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Challenge24 {

    private static final String TITLE_1 = "Arithmetic Logic Unit 1";
    private static final String TITLE_2 = "Arithmetic Logic Unit 2";

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final ALU alu = new ALU(lines);
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_1,
                System.currentTimeMillis() - start));
        return result;
    }

    private String padTo(final String s, final int size, final String replacement) {
        if (s.length() > size) {
            return new String(new char[size]).replace("\0", "-");
        }
        return new String(new char[size - s.length()]).replace("\0", replacement) + s;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("***  %s answer not found in %d ms",
                TITLE_2,
                System.currentTimeMillis() - start));
        return result;
    }

    public static final class ALU {

        Map<String, Integer> variables;

        private final Queue<Integer> input;

        private final String[] lines;

        public ALU(final String[] lines) {
            this.lines = lines;
            this.variables = new LinkedHashMap<>();
            this.variables.put("w", 0);
            this.variables.put("x", 0);
            this.variables.put("y", 0);
            this.variables.put("z", 0);
            this.input = new LinkedList<>();
        }

        public void run() {
            for (final String line : this.lines) {
                final String[] parts = line.split(" ");
                final String instruction = parts[0];
                final String operand1 = parts[1];
                final String operand2 = parts.length > 2 ? parts[2] : null;
//                System.out.println(instruction + ":" + operand1 + "," + operand2);
                switch (instruction) {
                    case "inp":
                        this.variables.put(operand1, this.input.poll());
                        break;
                    case "add":
                        this.variables.put(operand1, this.variables.get(operand1) + this.getNumberOrVariable(operand2));
                        break;
                    case "mul":
                        this.variables.put(operand1, this.variables.get(operand1) * this.getNumberOrVariable(operand2));
                        break;
                    case "div":
                        this.variables.put(operand1, Math.floorDiv(this.variables.get(operand1), this.getNumberOrVariable(operand2)));
                        break;
                    case "mod":
                        this.variables.put(operand1, this.variables.get(operand1) % this.getNumberOrVariable(operand2));
                        break;
                    case "eql":
                        this.variables.put(operand1, this.variables.get(operand1).equals(this.getNumberOrVariable(operand2)) ? 1 : 0);
                        break;
                }
            }
        }

        private int getNumberOrVariable(final String operand) {
            try {
                return Integer.parseInt(operand);
            } catch (final NumberFormatException ae) {
                return this.variables.get(operand);
            }
        }

        public void addInput(final int i) {
            this.input.add(i);
        }

        public void dump() {
            for (final Map.Entry<String, Integer> entry : this.variables.entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }
        }

        public void addMultipleInputs(final String s) {
            for (int i = 0; i < s.length(); i++) {
                this.addInput(Integer.parseInt(s.charAt(i) + ""));
            }
        }
    }
}
