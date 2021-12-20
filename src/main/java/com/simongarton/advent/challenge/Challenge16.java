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

    /*

    Finicky, indeed.

    I am circling around how to do this. I had initially tried a string with a packet reader; then tried to do it
    in one go - but that's not going to work, I don't have access to the bit string; so I'm back to a packet reader.
    Or not.

    This will have to maintain a pointer to the current index.

    We start with a hex string. This has to be converted to binary to make any sense (?) so I think the outer
    packet is a different kind of packet to the others.
    Reading the hex we use for the outer packet, we read 3 bits for the version (currently unused) and 3 bits for the
    type.
    If the type is 4, then we read a literal number - read the next bits in groups of 5 until we get a group starting with 0,
    stop there and figure out the number.
    Otherwise it's an operator packet : read an int for it's type, and then read packets by count or bits.


     */

    public void run(final String[] lines) {
        this.part1(lines);
        this.part2(lines);
    }

    protected long part1(final String[] lines) {
        final long start = System.currentTimeMillis();
        final long result = 0;
        final PacketReader packetReader = new PacketReader(lines[0]);
        packetReader.readPackets();
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

    public final static class Header {
        private int version;
        private int typeId;
        private final int bitsRead = 6;

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
        private final int lengthTypeMode;

        public Operator(final int lengthTypeMode, final int bitsRead) {
            this.bitsRead = bitsRead;
            this.lengthTypeMode = lengthTypeMode;
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

    public static final class Packet {
        private final Header header;
        private final PacketType packetType;
        private Integer literalValue;
        private List<Packet> subPackets;

        public Packet(final PacketType packetType, final Header header) {
            this.header = header;
            this.packetType = packetType;
            if (packetType == PacketType.OPERATOR) {
                this.subPackets = new ArrayList<>();
            }
        }
    }

    public static final class PacketReader {
        final private String hex;
        final private String binary;
        private int index;

        private Packet packet;

        public PacketReader(final String hex) {
            // we'll start by creating a packet based on this hex
            this.hex = hex;
            this.binary = new BigInteger(hex, 16).toString(2);
            System.out.println(hex);
            System.out.println(this.binary);
            this.index = 0;
            this.readPackets();
        }

        private void readPackets() {
            final Header header = this.readPacketHeader();
            System.out.println(header);
            if (header.typeId == 4) {
                PacketInteger packetInteger = this.readPacketAsLiteralValue();
                this.packet = new Packet(PacketType.LITERAL_VALUE, header);
                this.packet.literalValue = packetInteger.value;
                System.out.println(this.packet);
            } else {
                this.packet = this.readPacketAsOperator();
            }
        }

        private Header readPacketHeader() {
            final String versionString = this.binary.substring(this.index, this.index + 3);
            final String typeIdString = this.binary.substring(this.index + 3, this.index + 6);
            this.index += 6;
            final Header header = new Header();
            header.version = Integer.parseInt(versionString, 2);
            header.typeId = Integer.parseInt(typeIdString, 2);
            return header;
        }

        private PacketInteger readPacketAsLiteralValue() {
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

        // I am starting to add the logic in here for using a pointer - each time I have to read a packet,
        // I figure out the pointer, based on the index - the index might stay at the start of a sequence of
        // packets and subpackets, but the pointer needs to roam through them.
        private List<Packet> readPacketAsOperator(int pointer) {
            final Header header = this.readPacketHeader();
            System.out.println("Operator " + header);
            final PacketInteger lengthTypeId = this.readLengthTypeId();
            List<Packet> subPackets = new ArrayList<>();
            if (lengthTypeId.value == 1) {
                final PacketInteger totalLength = this.readTotalLength();
                return this.readSubpacketsOnLength(totalLength.value, pointer);
            } else {
                final PacketInteger packetCount = this.readSubPackets();
                return this.readSubpacketsOnCount(packetCount.value, pointer);
            }
        }

        private List<Packet> readSubpacketsOnLength(final int totalLength, int pointer) {
            int bitsRead = 0;
            while (true) {
                final Header header = this.readPacketHeader();
                System.out.println("Packet " + header);
                bitsRead += header.bitsRead;

                if (header.typeId == 4) {
                    final PacketInteger literalValue = this.readPacketAsLiteralValue();
                    bitsRead += literalValue.bitsRead;
                    System.out.println(literalValue);
                } else {
                    final PacketInteger operator = this.readPacketAsOperator();
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
    }
}
