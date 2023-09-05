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

import static com.deepthi8su.filesquash.huffman.ByteUtils.fromBytes;
import static com.deepthi8su.filesquash.huffman.HuffmanUtils.createHuffmanTree;


public class HuffmanDecoder {
    private static final Logger LOG = LogManager.getLogger(HuffmanDecoder.class);

    /**
     * Decodes a file using Huffman encoding.
     *
     * @param inputFileName  File to be decoded
     * @param outputFileName File to write decoded content
     */
    public void decode(String inputFileName, String outputFileName) {
        Path inputFile = Paths.get(inputFileName);
        Path outputFile = Paths.get(outputFileName);
        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile.toFile()));
             BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile.toString()))) {

            // read frequency map object len bytes
            byte[] frequencyMapBytesLenBytes = new byte[Integer.BYTES];
            inputStream.read(frequencyMapBytesLenBytes);
            int frequencyMapBytesLen = ByteBuffer.wrap(frequencyMapBytesLenBytes).getInt();

            // read frequency map object bytes
            byte[] frequencyMapBytes = new byte[frequencyMapBytesLen];
            inputStream.read(frequencyMapBytes);
            Map<Character, Long> frequencyMap = fromBytes(frequencyMapBytes);
            LOG.debug("Saved freq map is " + frequencyMap);

            decode(inputStream, outputStream, frequencyMap);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void decode(InputStream inputStream, OutputStream outputStream, Map<Character, Long> frequencyMap) throws IOException {
        HuffmanTreeNode root = createHuffmanTree(frequencyMap);
        if (root == null) {
            return;
        }

        // handle the case where the root is a leaf node i.e.,
        // the input string to encode is in format "aaaaa"
        if (root.isLeaf()) {
            long frequency = root.getFrequency();
            while (frequency-- > 0) {
                outputStream.write(root.getCharacter());
            }
        }

        // read original file size bytes
        byte[] originalFileSizeBytes = new byte[Long.BYTES];
        inputStream.read(originalFileSizeBytes);
        long originalFileSize = ByteBuffer.wrap(originalFileSizeBytes).getLong();

        HuffmanTreeNode node = root;
        int data;
        while ((data = inputStream.read()) != -1) {
            String s = String.format("%8s", Integer.toBinaryString(((byte) data) & 0xFF)).replace(' ', '0');
            for (char c : s.toCharArray()) {
                if (originalFileSize == 0) {
                    break;
                }
                if (node.isLeaf()) {
                    outputStream.write(node.getCharacter());
                    node = root;
                    originalFileSize--;
                }
                if (c == '0') {
                    node = node.hasLeft() ? node.getLeft() : root;
                }
                if (c == '1') {
                    node = node.hasRight() ? node.getRight() : root;
                }
            }
        }
        if (originalFileSize > 0 && node != null) {
            outputStream.write(node.getCharacter());
        }
        outputStream.flush();
    }
}
