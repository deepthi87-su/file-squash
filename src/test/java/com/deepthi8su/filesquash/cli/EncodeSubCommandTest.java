package com.deepthi8su.filesquash.cli;

import com.deepthi8su.filesquash.huffman.HuffmanEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class EncodeSubCommandTest {

    @Mock
    HuffmanEncoder huffmanEncoder;

    @Test
    public void testRun() {
        String inputFile = "test.txt";
        String outputFile = "test.squash";

        lenient().doNothing().when(huffmanEncoder).encode(inputFile, outputFile);
        CommandLine cmd = new CommandLine(new EncodeSubCommand(huffmanEncoder));
        assertEquals(0, cmd.execute("-i " + inputFile, "-o " + outputFile));
    }

    @Test
    public void testRun_whenInputFileIsInvalid() {
        String inputFile = "test1.txt";
        String outputFile = "test2.txt";

        lenient().doNothing().when(huffmanEncoder).encode(inputFile, outputFile);
        CommandLine cmd = new CommandLine(new EncodeSubCommand(huffmanEncoder));
        assertEquals(1, cmd.execute("-i " + inputFile, "-o " + outputFile));
    }

}
