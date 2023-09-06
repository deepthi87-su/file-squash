# file-squash

A CLI to for lossless compression of text files, employing the Huffman encoding technique written in Java.

## Demo
[![asciicast](https://asciinema.org/a/XI1P8ROvT8wSGiBSi5k0bJ6vA.svg)](https://asciinema.org/a/XI1P8ROvT8wSGiBSi5k0bJ6vA)

## Prerequisites
* [JDK 11](https://jdk.java.net/archive/)
* [Maven 3.9.1](https://maven.apache.org/docs/3.9.1/release-notes.html)

If you are on a Mac you can install these using brew.
```shell
$ brew install openjdk@11
$ brew install maven@3.9.1
```

## Usage Instructions
To build the jar package for local execution, run
```shell
$ mvn package
```
To execute the jar package created by above command, run
```shell
$ java -jar target/file-squash-1.0-SNAPSHOT-jar-with-dependencies.jar
Usage: fsquash [COMMAND]
File Squash. A CLI to compress and decompress files using Huffman coding.
Commands:
  encode    Squashes given input file.
  decode    Decodes given squashed input file.
  generate  Generates a file with given size with random text data.
```


## Local Development
To execute unit tests, run
```shell
$ mvn test
```
To view code coverage reports run
```shell
$ open ./target/site/jacoco/index.html
```
Above command should open code coverage reports in browser
![Alt text](./images/file-squash-code-coverage.png?raw=true "Title")
