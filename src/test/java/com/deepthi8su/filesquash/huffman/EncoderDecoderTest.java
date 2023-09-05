package com.deepthi8su.filesquash.huffman;

import com.deepthi8su.filesquash.generator.RandomFileGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class EncoderDecoderTest {

    private static final String testTxtFileName = "test.txt";
    private static final String testSquashedFileName = "test.squash";
    private static final String testUnSquashedFileName = "test-unsquashed.txt";

    @TempDir
    static Path tempDir;

    static Path originalTxtFile;
    static Path squashedFile;
    static Path unSquashedFile;

    @BeforeAll
    public static void init() throws IOException {
        originalTxtFile = Files.createFile(tempDir.resolve(testTxtFileName));
        squashedFile = Files.createFile(tempDir.resolve(testSquashedFileName));
        unSquashedFile = Files.createFile(tempDir.resolve(testUnSquashedFileName));
    }

    @Test
    void testEncodeDecode_whenInputFileIsEmpty() {
        new HuffmanEncoder().encode(originalTxtFile.toString(), squashedFile.toString());
        new HuffmanDecoder().decode(squashedFile.toString(), unSquashedFile.toString());
        assertAll(() -> assertLinesMatch(Files.readAllLines(originalTxtFile), Files.readAllLines(unSquashedFile)));
    }

    @Test
    void testEncodeDecode_whenInputFileIsNotEmpty() {
        RandomFileGenerator.generate(originalTxtFile.toString(), 1000L);
        new HuffmanEncoder().encode(originalTxtFile.toString(), squashedFile.toString());
        new HuffmanDecoder().decode(squashedFile.toString(), unSquashedFile.toString());
        assertAll(
                () -> assertFalse(Files.readAllLines(originalTxtFile).isEmpty()),
                () -> assertLinesMatch(Files.readAllLines(originalTxtFile), Files.readAllLines(unSquashedFile))
        );
    }
}
