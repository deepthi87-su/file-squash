package com.deepthi8su.filesquash.cli;

import com.deepthi8su.filesquash.huffman.HuffmanDecoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class DecodeSubCommandTest {

    @Mock
    HuffmanDecoder huffmanDecoder;

    @Test
    public void testRun() {
        String inputFile = "test.squash";
        String outputFile = "test.txt";

        lenient().doNothing().when(huffmanDecoder).decode(inputFile, outputFile);
        CommandLine cmd = new CommandLine(new DecodeSubCommand(huffmanDecoder));
        assertEquals(0, cmd.execute("-i " + inputFile, "-o " + outputFile));
    }

    @Test
    public void testRun_whenInputFileIsInvalid() {
        String inputFile = "test1.txt";
        String outputFile = "test2.txt";

        lenient().doNothing().when(huffmanDecoder).decode(inputFile, outputFile);
        CommandLine cmd = new CommandLine(new DecodeSubCommand(huffmanDecoder));
        assertEquals(1, cmd.execute("-i " + inputFile, "-o " + outputFile));
    }

}
