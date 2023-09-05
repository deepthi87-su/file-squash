package com.deepthi8su.filesquash.huffman;

import java.util.Arrays;
import java.util.HashSet;

/**
 * To hold the character and the frequency of the character
 */
public class HuffmanTreeNode {
    /**
     * The character that this node represents
     */
    private final char character;

    /**
     * Frequency of the character
     */
    private final long frequency;

    /**
     * Huffman code of the character
     */
    private String code;

    private final HuffmanTreeNode left;
    private final HuffmanTreeNode right;

    private static final Character[] punctuationChars = new Character[] {'!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '`', '{', '|', '}'};
    public static final HashSet<Character> punctuationCharSet = new HashSet<>(Arrays.asList(punctuationChars));

    public HuffmanTreeNode(Character character, long frequency) {
        if (!Character.isAlphabetic(character)
                && !Character.isDigit(character)
                && !Character.isWhitespace(character)
                && !punctuationCharSet.contains(character)) {
            throw new RuntimeException(
                    String.format("Unsupported char: '%c'. Supported chars are: a-z, A-Z, 0-9, whitespace, punctuation",
                            character));
        }
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public HuffmanTreeNode(long frequency, HuffmanTreeNode left, HuffmanTreeNode right) {
        this.character = Character.MIN_VALUE;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    public char getCharacter() {
        return character;
    }

    public long getFrequency() {
        return frequency;
    }

    public HuffmanTreeNode getLeft() {
        return left;
    }

    public HuffmanTreeNode getRight() {
        return right;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }
}
