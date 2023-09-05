package com.deepthi8su.filesquash.cli;

import com.deepthi8su.filesquash.huffman.HuffmanEncoder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "encode",
        description = "Squashes given input file.")
public class EncodeSubCommand implements Runnable, CommandLine.IExitCodeGenerator {

    @Option(names = {"-i", "--input-file"},
            description = "Input file path with text content to encode. . Example: -o /path/to/file.txt",
            required = true)
    String inputFile;

    @Option(names = {"-o", "--output-file"},
            description = "Output file path to write encoded content. . Example: -o /path/to/file.squash")
    String outputFile;

    private final HuffmanEncoder huffmanEncoder;
    private int exitCode = 0;

    public EncodeSubCommand() {
        this.huffmanEncoder = new HuffmanEncoder();
    }

    EncodeSubCommand(HuffmanEncoder huffmanEncoder) {
        this.huffmanEncoder = huffmanEncoder;
    }

    @Override
    public void run() {
        if (outputFile == null) {
            outputFile = inputFile + ".squash";
        }
        if (!outputFile.endsWith(".squash")) {
            System.out.println("Output file extension should be `.squash`. Provided " + outputFile);
            exitCode = 1;
            return;
        }
        huffmanEncoder.encode(inputFile, outputFile);
        System.out.println("Created " + outputFile);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
