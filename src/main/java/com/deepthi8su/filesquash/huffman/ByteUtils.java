package com.deepthi8su.filesquash.huffman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

/**
 * Utility class for converting between byte arrays and maps
 */
public class ByteUtils {

    /**
     * Serializes a map to a byte array
     *
     * @param frequencyMap Map to serialize
     * @return Byte array containing serialized map
     * @throws IOException If an error occurs while serializing the map
     * @see #fromBytes(byte[])
     */
    public static byte[] toBytes(Map<Character, Long> frequencyMap) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(frequencyMap);
            return bos.toByteArray();
        }
    }

    /**
     * Deserializes a byte array to a map
     *
     * @param bytes Byte array containing serialized map
     * @return Deserialized map
     * @throws IOException If an error occurs while deserializing the map
     * @throws ClassNotFoundException If the map class is not found
     * @see #toBytes(Map)
     */
    public static Map<Character, Long> fromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(bis)) {
            Object o = in.readObject();
            if (!(o instanceof Map)) {
                throw new RuntimeException(".squash file is currupted!");
            }
            return (Map<Character, Long>) o;
        }
    }

    /**
     * Utility method to copy the string builder to a byte buffer
     *
     * @param sb String builder to copy characters from
     * @param buffer Byte buffer to write the characters to
     * @return The string builder with the characters that were not copied as buffer was full
     */
    public static StringBuilder copyToByteBuffer(StringBuilder sb, byte[] buffer) {
        int i;
        for (i = 0; i < (buffer.length * 8); i++) {
            char c = sb.charAt(i);
            if (c == '1') {
                buffer[i >> 3] |= (byte) (0x80 >> (i & 0x7));
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid char in binary string. char: " + c + " at index: " + i);
            }
        }

        StringBuilder remaining = new StringBuilder();
        for (; i < sb.length(); i++) {
            remaining.append(sb.charAt(i));
        }
        return remaining;
    }
}
