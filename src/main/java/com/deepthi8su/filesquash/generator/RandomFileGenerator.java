package com.deepthi8su.filesquash.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static com.deepthi8su.filesquash.huffman.HuffmanTreeNode.punctuationCharSet;

public class RandomFileGenerator {

    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789 \n\t"
            + punctuationCharSet.stream().map(String::valueOf).collect(Collectors.joining());


    /**
     * Generates a random file of length <code>bytesLen</code>
     *
     * @param fileName name of the file to generate
     * @param bytesLen length of the file to generate
     */
    public static void generate(String fileName, long bytesLen) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName))) {
            while (bytesLen-- > 0) {
                int index = (int) (ALLOWED_CHARS.length() * Math.random());
                writer.write(ALLOWED_CHARS.charAt(index));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
