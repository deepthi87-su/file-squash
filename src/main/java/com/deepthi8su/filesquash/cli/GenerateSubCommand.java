package com.deepthi8su.filesquash.cli;


import com.deepthi8su.filesquash.generator.RandomFileGenerator;
import picocli.CommandLine;

@CommandLine.Command(name = "generate",
        description = "Generates a file with given size with random text data.")
public class GenerateSubCommand implements Runnable {

    @CommandLine.Option(names = {"-n", "--size-in-bytes"},
            description = "File size in bytes. Default is 500KB. Example: -n 512000")
    long size = 512000L;

    @CommandLine.Option(names = {"-o", "--output-file"},
            required = true,
            description = "Output file path to write decoded content. Example: -o /path/to/file.txt")
    String outputFile;

    @Override
    public void run() {
        RandomFileGenerator.generate(outputFile, size);
        System.out.println("Created " + outputFile);
    }
}
