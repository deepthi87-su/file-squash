package com.deepthi8su.filesquash.huffman;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanUtils {

    private static final Logger LOG = LogManager.getLogger(HuffmanUtils.class);

    /**
     * Creates a frequency map for the given file
     *
     * @param inputFile File to create frequency map for
     * @return Frequency map
     * @throws IOException If an error occurs while reading the file
     */
    public static Map<Character, Long> createFrequencyMap(Path inputFile) throws IOException {
        Map<Character, Long> frequencyMap = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
            int c;
            while ((c = reader.read()) != -1) {
                frequencyMap.merge((char) c, 1L, Long::sum);
            }
        }
        LOG.debug("frequencyMap is: " + frequencyMap);
        return frequencyMap;
    }

    /**
     * Creates a Huffman tree from the given frequency map
     *
     * <p>
      * The tree is created by merging the nodes with the lowest frequencies.
      * The resulting tree is a complete binary tree.
     * </p>
     *
     * @param frequencyMap Frequency map
     * @return Root node of the Huffman tree
     */
    public static HuffmanTreeNode createHuffmanTree(Map<Character, Long> frequencyMap) {
        PriorityQueue<HuffmanTreeNode> minHeap = new PriorityQueue<>(
                Comparator.comparingLong(HuffmanTreeNode::getFrequency));
        for (var entry : frequencyMap.entrySet()) {
            minHeap.add(new HuffmanTreeNode(entry.getKey(), entry.getValue()));
        }

        while (minHeap.size() > 1) {
            HuffmanTreeNode left = minHeap.poll();
            HuffmanTreeNode right = minHeap.poll();

            long sum = left.getFrequency() + right.getFrequency();
            minHeap.add(new HuffmanTreeNode(sum, left, right));
        }
        return minHeap.peek();
    }

    /**
     * Creates a Huffman code map for the given Huffman tree
     *
     * <p>
     * The code is created by traversing the tree in pre-order fashion.
     * </p>
     *
     * @param root Root node of the Huffman tree
     * @return Huffman code map corresponding to each character in the tree
     * @see HuffmanTreeNode
     */
    public static Map<Character, String> createHuffmanCodeMap(HuffmanTreeNode root) {
        Map<Character, String> huffmanCodeMap = new HashMap<>();
        if (root == null) {
            return huffmanCodeMap;
        }

        Deque<HuffmanTreeNode> stack = new ArrayDeque<>();
        root.setCode("");
        stack.push(root);
        while (!stack.isEmpty()) {
            HuffmanTreeNode node = stack.pop();
            String code = node.getCode();
            if (node.isLeaf()) {
                huffmanCodeMap.put(node.getCharacter(), code);
            }
            if (node.hasLeft()) {
                node.getLeft().setCode(code + "0");
                stack.push(node.getLeft());
            }
            if (node.hasRight()) {
                node.getRight().setCode(code + "1");
                stack.push(node.getRight());
            }
        }
        LOG.debug("Huffman code map is: " + huffmanCodeMap);
        return huffmanCodeMap;
    }
}
