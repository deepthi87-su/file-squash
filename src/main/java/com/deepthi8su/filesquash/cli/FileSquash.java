package com.deepthi8su.filesquash.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
        name = "file-squash",
        version = "1.0",
        description = "A CLI to compress and decompress files using Huffman encoding.",
        subcommands = {
                EncodeSubCommand.class,
                DecodeSubCommand.class,
                GenerateSubCommand.class
        }
)
public class FileSquash implements Runnable {

    @Override
    public void run() {
        new CommandLine(new FileSquash()).usage(System.out);
    }
}
