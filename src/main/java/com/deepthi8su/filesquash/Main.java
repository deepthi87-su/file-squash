package com.deepthi8su.filesquash;

import com.deepthi8su.filesquash.cli.FileSquash;
import picocli.CommandLine;

public class Main {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new FileSquash()).execute(args);
        System.exit(exitCode);
    }
}
