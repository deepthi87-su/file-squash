package com.deepthi8su.filesquash.cli;

import com.deepthi8su.filesquash.huffman.HuffmanDecoder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "decode",
        description = "Decodes given squashed input file.")
public class DecodeSubCommand implements Runnable, CommandLine.IExitCodeGenerator {

    @Option(names = {"-i", "--input-file"},
            description = "Input file path with encoded content. Example: -o /path/to/file.squash",
            required = true)
    String inputFile;

    @Option(names = {"-o", "--output-file"},
            description = "Output file path to write decoded content. Example: -o /path/to/file.txt")
    String outputFile;

    private final HuffmanDecoder huffmanDecoder;
    private int exitCode = 0;

    public DecodeSubCommand() {
        this.huffmanDecoder = new HuffmanDecoder();
    }

    DecodeSubCommand(HuffmanDecoder huffmanDecoder) {
        this.huffmanDecoder = huffmanDecoder;
    }

    @Override
    public void run() {
        if (!inputFile.endsWith(".squash")) {
            System.out.println("Input file extension should be `.squash`. Provided " + inputFile);
            exitCode = 1;
            return;
        }
        if (outputFile == null) {
            outputFile = inputFile.substring(0, inputFile.lastIndexOf('.'));
        }
        huffmanDecoder.decode(inputFile, outputFile);
        System.out.println("Created " + outputFile);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
