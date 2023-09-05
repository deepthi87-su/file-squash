package com.deepthi8su.filesquash.huffman;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static com.deepthi8su.filesquash.huffman.ByteUtils.copyToByteBuffer;
import static com.deepthi8su.filesquash.huffman.ByteUtils.toBytes;
import static com.deepthi8su.filesquash.huffman.HuffmanUtils.createFrequencyMap;
import static com.deepthi8su.filesquash.huffman.HuffmanUtils.createHuffmanCodeMap;
import static com.deepthi8su.filesquash.huffman.HuffmanUtils.createHuffmanTree;


public class HuffmanEncoder {

    private static final Logger LOG = LogManager.getLogger(HuffmanEncoder.class);

    private static final int BUFFER_LEN = 8192; // size of byte buffer to write to file

    /**
     * Encodes a file using Huffman encoding.
     *
     * @param inputFileName    File to be encoded
     * @param outputFileName   File to write encoded content
     */
    public void encode(String inputFileName, String outputFileName) {
        if (!outputFileName.endsWith(".squash")) {
            throw new IllegalArgumentException("Output file extension should be `.squash`. Provided " + outputFileName);
        }

        Path inputFile = Paths.get(inputFileName);
        Path outputFile = Paths.get(outputFileName);
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile.toFile()));
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile.toString()))) {
            encode(inputStream, outputStream, createFrequencyMap(inputFile));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    private static void encode(InputStream inputStream, OutputStream outputStream, Map<Character, Long> frequencyMap) throws IOException {
        Map<Character, String> huffmanCodeMap = createHuffmanCodeMap(createHuffmanTree(frequencyMap));
        StringBuilder sb = new StringBuilder();

        // write frequencyMap to file to decompress the file later
        byte[] frequencyMapBytes = toBytes(frequencyMap);
        outputStream.write(ByteBuffer.allocate(Integer.BYTES).putInt(frequencyMapBytes.length).array());
        outputStream.write(frequencyMapBytes);

        // write input file size to file to remember number of bytes to read later when decompressing file
        long inputFileSize = frequencyMap.values().stream().mapToLong(Long::longValue).sum();
        outputStream.write(ByteBuffer.allocate(Long.BYTES).putLong(inputFileSize).array());

        int ch;
        while ((ch = inputStream.read()) != -1) {
            String code = huffmanCodeMap.get((char) ch);
            sb.append(code);

            // write to file if buffer is full
            if (sb.length() >= (Byte.SIZE * BUFFER_LEN)) {
                byte[] buffer = new byte[BUFFER_LEN];
                sb = copyToByteBuffer(sb, buffer);
                outputStream.write(buffer);
            }
        }

        // pad the last byte with 0's
        int paddingLen = Byte.SIZE - (sb.length() % Byte.SIZE);
        while (paddingLen > 0 && paddingLen < Byte.SIZE) {
            sb.append("0");
            paddingLen--;
        }

        // write remaining bytes
        if (sb.length() > 0) {
            byte[] buffer = new byte[sb.length() / Byte.SIZE];
            copyToByteBuffer(sb, buffer);
            outputStream.write(buffer);
        }
        outputStream.flush();
    }
}
