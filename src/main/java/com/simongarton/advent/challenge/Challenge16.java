package com.simongarton.advent.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Challenge16 {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private static final String TITLE_1 = "Packet Decoder 1";
    private static final String TITLE_2 = "Packet Decoder 2";
    private static final boolean DEBUG = true;
    private final Map<String, String> binaryTable = new HashMap();

    public void run(final String[] lines) {
        this.buildBinaryTable();
        this.part1(lines);
        this.part2(lines);
    }

    public void debug(final String[] lines) {
        this.buildBinaryTable();
        for (final String hex : lines) {
            final String binary = readPaddedBinaryFromHex(hex);
            final Packet packet = new Packet(binary, 0);
            System.out.println("versionSum " + packet.versionSum());
        }
    }

    private String readPaddedBinaryFromHex(final String hex) {
        StringBuilder paddedBinary = new StringBuilder();
        for (int i = 0; i < hex.length(); i++) {
            final String c = hex.charAt(i) + "";
            paddedBinary.append(this.binaryTable.get(c));
        }
        return paddedBinary.toString();
    }

    private void buildBinaryTable() {
        this.binaryTable.clear();
        for (int i = 0; i < 16; i++) {
            final String hex = Integer.toString(i, 16);
            final String binary = this.padTo("" + new BigInteger(hex, 16).toString(2), 4, "0");
            this.binaryTable.put(hex.toUpperCase(Locale.ROOT), binary);
        }
    }

    private String padTo(final String s, final int size) {
        return this.padTo(s, size, " ");
    }

    private String padTo(final String s, final int size, final String replacement) {
        if (s.length() > size) {
            return new String(new char[size]).replace("\0", "*");
        }
        return new String(new char[size - s.length()]).replace("\0", replacement) + s;
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final String hex = lines[0];
        final String binary = readPaddedBinaryFromHex(hex);
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
        private Long literalValue;
        private List<Packet> subPackets;

        public Packet(final String originalBinary, final int index) {
            this.log("", "");
            this.originalBinary = originalBinary;
            this.log("originalBinary ", originalBinary);
            this.header = new Header(originalBinary.substring(0, 6));
            this.bitsRead += 6;
            this.packetType = this.figurePacketType(this.header);
            this.log("header ", this.header.version + ":" + this.header.typeId + " = " + this.packetType);
            this.bitsRead += this.buildPacket(this.originalBinary.substring(6));
            this.log("Packet", this.toString());
        }

        @Override
        public String toString() {
            if (this.packetType == PacketType.LITERAL_VALUE) {
                return "Packet[" + this.packetType + " " + this.literalValue + "]";
            } else {
                return "Packet[" + this.packetType + " " + this.subPackets.size() + "]";
            }
        }

        private void log(final String caption, final String value) {
            if (DEBUG) {
                System.out.println(Challenge16.this.padTo(caption, 20) + " : " + value);
            }
        }

        private int buildPacket(final String binary) {
            switch (this.packetType) {
                case LITERAL_VALUE:
                    return this.buildPacketFromLiteralValue(binary);
                case OPERATOR:
                default:
                    return this.buildPacketFromOperator(binary);
            }
        }

        private int buildPacketFromOperator(final String binary) {
            int bitsReadForOperator = 0;
            final int lengthTypeId = Integer.parseInt(binary.substring(0, 1));
            bitsReadForOperator += 1;
            switch (lengthTypeId) {
                case 0:
                    return bitsReadForOperator + this.readOperatorPacketsFromLength(binary.substring(1));
                case 1:
                default:
                    return bitsReadForOperator + this.readOperatorPacketsFromCount(binary.substring(1));
            }
        }

        private int readOperatorPacketsFromCount(final String substring) {
            log("countSubstring",substring);
            int bitsReadForOperatorLength = 0;
            final int subPacketsCount = Integer.parseInt(substring.substring(0, 11), 2);
            log("countSubPackets",subPacketsCount + "");
            bitsReadForOperatorLength += 11;
            this.subPackets = new ArrayList<>();
            int subPacketsBitsRead = 0;
            while (subPackets.size() < subPacketsCount) {
                String subsubString = substring.substring(11 + subPacketsBitsRead);
                log("index",subPackets.size() + " " + subsubString);
                final Packet packet = new Packet(subsubString, 0);
                subPackets.add(packet);
                subPacketsBitsRead += packet.bitsRead;
            }
            return bitsReadForOperatorLength + subPacketsBitsRead;
        }

        private int readOperatorPacketsFromLength(final String substring) {
            int bitsReadForOperatorLength = 0;
            final int subPacketsBitLength = Integer.parseInt(substring.substring(0, 15), 2);
            bitsReadForOperatorLength += 15;
            int subPacketsBitsRead = 0;
            this.subPackets = new ArrayList<>();
            while (subPacketsBitsRead < subPacketsBitLength) {
                final Packet packet = new Packet(substring.substring(15 + subPacketsBitsRead), 0);
                this.subPackets.add(packet);
                subPacketsBitsRead += packet.bitsRead;
            }
            return bitsReadForOperatorLength + subPacketsBitsRead;
        }

        private int buildPacketFromLiteralValue(final String binary) {
            int bitsReadForLiteralValue = 0;
            final List<FiveBitInteger> numbers = new ArrayList<>();
            while (true) {
                final String numberString = binary.substring(bitsReadForLiteralValue, bitsReadForLiteralValue + 5);
                bitsReadForLiteralValue += 5;
                final FiveBitInteger number = new FiveBitInteger(numberString);
                numbers.add(number);
                if (number.lastBit) {
                    break;
                }
            }
            final String binaryString = numbers.stream().map(n -> n.value).collect(Collectors.joining());
            this.log("binaryString ", binaryString);
            this.literalValue = Long.parseLong(binaryString, 2);
            this.log("literalValue ", this.literalValue + "");
            return bitsReadForLiteralValue;
        }

        private PacketType figurePacketType(final Header header) {
            switch (header.typeId) {
                case 4:
                    return PacketType.LITERAL_VALUE;
                default:
                    return PacketType.OPERATOR;
            }
        }

        public int versionSum() {
            int versionSum = this.header.version;
            if (this.subPackets != null) {
                for (Packet subPacket : this.subPackets) {
                    versionSum += subPacket.versionSum();
                }
            }
            return versionSum;
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
