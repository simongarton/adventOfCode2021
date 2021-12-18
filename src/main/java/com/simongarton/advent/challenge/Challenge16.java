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

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final PacketReader packetReader = new PacketReader(lines[0]);
        packetReader.run();
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

    public final static class PacketReader {
        final private String hex;
        final private String binary;
        private int index;

        public PacketReader(final String hex) {
            this.hex = hex;
            this.binary = new BigInteger(hex, 16).toString(2);
            System.out.println(hex);
            System.out.println(this.binary);
            this.index = 0;
        }

        public void run() {
            final Header header = this.readHeader();
            System.out.println(header);
            if (header.typeId == 4) {
                System.out.println(this.readLiteralValue());
            } else {
                this.readOperator();
            }
        }

        private Operator readOperator() {
            final Header header = this.readHeader();
            System.out.println("Operator " + header);
            final PacketInteger lengthTypeId = this.readLengthTypeId();
            if (lengthTypeId.value == 1) {
                final PacketInteger totalLength = this.readTotalLength();
                this.readSubpacketsOnLength(totalLength.value);
            } else {
                final PacketInteger packetCount = this.readSubPackets();
                this.readSubpacketsOnCount(packetCount.value);
            }
        }

        private void readSubpacketsOnLength(final int totalLength) {
            int bitsRead = 0;
            while (true) {
                final Header header = this.readHeader();
                System.out.println("Packet " + header);
                bitsRead += header.bitsRead;

                if (header.typeId == 4) {
                    PacketInteger literalValue = this.readLiteralValue();
                    bitsRead += literalValue.bitsRead;
                    System.out.println(literalValue);
                } else {
                    PacketInteger operator = this.readOperator();
                }
            }
        }

        private PacketInteger readLengthTypeId() {
            return this.readSimpleBinaryValue(1);
        }

        private PacketInteger readTotalLength() {
            return this.readSimpleBinaryValue(15);
        }

        private PacketInteger readSubPackets() {
            return this.readSimpleBinaryValue(11);
        }

        private PacketInteger readSimpleBinaryValue(final int digits) {
            final String lengthString = this.binary.substring(this.index, this.index + digits);
            this.index += digits;
            return new PacketInteger(digits, Integer.parseInt(lengthString, 2));
        }

        private PacketInteger readLiteralValue() {
            final int initialIndex = this.index;
            final List<FiveBitInteger> numbers = new ArrayList<>();
            while (true) {
                final String numberString = this.binary.substring(this.index, this.index + 5);
                this.index += 5;
                final FiveBitInteger number = new FiveBitInteger(numberString);
                System.out.println("  " + number);
                numbers.add(number);
                if (number.lastBit) {
                    break;
                }
            }
            final String hexString = numbers.stream().map(n -> n.value).collect(Collectors.joining());
            return new PacketInteger(this.index - initialIndex, Integer.parseInt(hexString, 2));
        }

        public Header readHeader() {
            final String versionString = this.binary.substring(this.index, this.index + 3);
            final String typeIdString = this.binary.substring(this.index + 3, this.index + 6);
            this.index += 6;
            final Header header = new Header();
            header.version = Integer.parseInt(versionString, 2);
            header.typeId = Integer.parseInt(typeIdString, 2);
            return header;
        }

        public static final class Packet {
            private Header header;
        }

        public final static class Header {
            private int version;
            private int typeId;
            private int bitsRead = 6;

            @Override
            public String toString() {
                return "Header(" + this.version + "," + this.typeId + ")";
            }
        }

        public final static class PacketInteger {
            private final int value;
            private final int bitsRead;

            public PacketInteger(final int value, final int bitsRead) {
                this.value = value;
                this.bitsRead = bitsRead;
            }
        }

        public final static class Operator {
            private final int bitsRead;

            public Operator(final int bitsRead) {
                this.bitsRead = bitsRead;
            }
        }

        public final static class FiveBitInteger {
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
                return "fiveBitInteger(" + this.lastBit + ":" + this.value + ")";
            }
        }
    }
}
