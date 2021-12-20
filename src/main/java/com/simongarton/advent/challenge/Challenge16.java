package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Challenge16 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Packet Decoder 1";
    private static final String TITLE_2 = "Packet Decoder 2";
    private static final boolean DEBUG = true;

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final String hex = lines[0];
        final String binary = new BigInteger(hex, 16).toString(2);
        final Packet packet = new Packet(binary, 0);
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_1,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    protected long part2(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        this.logger.info(String.format("%s answer %d complete in %d ms",
                TITLE_2,
                result,
                System.currentTimeMillis() - start));
        return result;
    }

    public enum PacketType {
        LITERAL_VALUE,
        OPERATOR
    }

    public final class Packet {

        private final String originalBinary;
        private int bitsRead;
        private final Header header;
        private final PacketType packetType;
        private Integer literalValue;

        public Packet(final String originalBinary, final int index) {
            this.originalBinary = originalBinary;
            this.log("originalBinary ", originalBinary);
            this.header = new Header(originalBinary.substring(0, 6));
            this.bitsRead += 6;
            this.packetType = this.figurePacketType(this.header);
            this.log("header ", this.header.version + ":" + this.header.typeId + " = " + this.packetType);
            this.bitsRead += this.buildPacket(this.originalBinary.substring(6));
        }

        private void log(final String caption, final String value) {
            if (DEBUG) {
                System.out.println(this.padTo(caption, 20) + " : " + value);
            }
        }

        private String padTo(final String s, final int size) {
            if (s.length() > size) {
                return new String(new char[size]).replace("\0", "*");
            }
            return new String(new char[size - s.length()]).replace("\0", " ") + s;
        }

        private int buildPacket(final String binary) {
            switch (this.packetType) {
                case LITERAL_VALUE:
                    return this.buildPacketFromLiteralValue(binary);
                default:
                    return this.buildPacketFromOperator(binary);
            }
        }

        private int buildPacketFromOperator(final String binary) {
            return 0;
        }

        private int buildPacketFromLiteralValue(final String binary) {
            int index = 0;
            final List<FiveBitInteger> numbers = new ArrayList<>();
            while (true) {
                final String numberString = binary.substring(index, index + 5);
                index += 5;
                final FiveBitInteger number = new FiveBitInteger(numberString);
                numbers.add(number);
                if (number.lastBit) {
                    break;
                }
            }
            final String hexString = numbers.stream().map(n -> n.value).collect(Collectors.joining());
            this.log("hexString ", hexString);
            this.literalValue = Integer.parseInt(hexString, 2);
            this.log("literalValue ", this.literalValue + "");
            return index;
        }

        private PacketType figurePacketType(final Header header) {
            switch (header.typeId) {
                case 4:
                    return PacketType.LITERAL_VALUE;
                default:
                    return PacketType.OPERATOR;
            }
        }

        public final class Header {

            private final String binary;

            private final int version;
            private final int typeId;

            public Header(final String binary) {
                this.binary = binary;

                final String versionString = binary.substring(0, 3);
                final String typeIdString = binary.substring(3, 6);
                this.version = Integer.parseInt(versionString, 2);
                this.typeId = Integer.parseInt(typeIdString, 2);
            }
        }

        public final class FiveBitInteger {
            private final String bits;
            private boolean lastBit = false;
            private final String value;

            public FiveBitInteger(final String bits) {
                this.bits = bits;
                this.lastBit = bits.charAt(0) == '0';
                this.value = bits.substring(1, 5);
            }

            @Override
            public String toString() {
                return "FiveBitInteger(" + this.lastBit + ":" + this.value + ")";
            }
        }
    }
}
