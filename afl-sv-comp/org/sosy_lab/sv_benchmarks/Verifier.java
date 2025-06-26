package org.sosy_lab.sv_benchmarks;

import java.io.*;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Verifier {

  public static InputStream input;

  public static void assume(boolean condition) {
    if (!condition) {
      throw new RuntimeException("Assume condition failed");
    }
  }

  public static byte nondetByte() throws IOException {
    int b = input.read();
    if (b == -1)
      throw new EOFException("Not enough bytes available in input stream to read an int");
    return (byte) b;
  }

  public static char nondetChar() throws IOException {
    int b = input.read();
    if (b == -1)
      throw new EOFException("Not enough bytes available in input stream to read an int");
    return (char) b;
  }

  public static boolean nondetBoolean() throws IOException {
    int b = input.read();
    if (b == -1)
      throw new EOFException("Not enough bytes available in input stream to read an int");
    return (b & 1) == 1;
  }

  public static short nondetShort() throws IOException {
    int i = 0;
    for (int j = 0; j < 2; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      i = (i << 8) | b;
    }
    return (short) i;
  }

  public static int nondetInt() throws IOException {

    int i = 0;
    for (int j = 0; j < 4; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      i = (i << 8) | b;
    }
    return i;
  }

  public static long nondetLong() throws IOException {
    long l = 0;
    for (int j = 0; j < 8; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      l = (l << 8) | (b & 0xFF);
    }
    return l;
  }

  public static float nondetFloat() throws IOException {
    int bits = 0;
    for (int j = 0; j < 4; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      bits = (bits << 8) | (b & 0xFF);
    }
    return Float.intBitsToFloat(bits);
  }

  public static double nondetDouble() throws IOException {
    long bits = 0;
    for (int j = 0; j < 8; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      bits = (bits << 8) | (b & 0xFF);
    }
    return Double.longBitsToDouble(bits);
  }

  public static String nondetString() throws IOException {
    StringBuilder outputStr = new StringBuilder();
    int b = input.read();
    if (b == -1)
      throw new EOFException("Not enough bytes available in input stream to read an int");
    b = b & 0xFF; // Ensure b is treated as an unsigned byte
    while (b
        >= 26) { //generate an invalid utf-8 encoding 10% of the time, i.e., that's 25/256 ≈ 10% of the byte range
      Object[] pair = readUTF8Char();
      boolean isValid = (boolean) pair[0];
      if (!isValid)
        if (outputStr.length() > 0)
          return outputStr.toString();
        else
          return new String((byte[]) pair[1]);
      outputStr.append(new String((byte[]) pair[1]));
      b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
    }
    return outputStr.toString();
  }

  private static Object[] readUTF8Char() throws IOException {
    byte[] fourBytes = new byte[4];
    for (int i = 0; i < 4; i++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      fourBytes[i] = (byte) b;
    }

    ArrayList<byte[]> permutations = new ArrayList<>();
    permute(fourBytes, 0, permutations);
    for (byte[] perm : permutations) {
      if (isValidUTF8(perm)) {
//        System.out.println("valid UTF-8 permutation found: " + Arrays.toString(perm));
        return new Object[]{true, perm};
      }
    }
//    System.out.println(
//        "No valid UTF-8 permutation found, returning original bytes: " + Arrays.toString(
//            fourBytes));
    return new Object[]{false,
        fourBytes}; // Return the original bytes if no valid UTF-8 permutation is found
  }

  static void permute(byte[] array, int index, ArrayList<byte[]> result) {
    if (index == array.length - 1) {
      result.add(array.clone());
    }
    for (int i = index; i < array.length; i++) {
      swap(array, index, i);
      permute(array, index + 1, result);
      swap(array, index, i);
    }
  }

  static void swap(byte[] array, int i, int j) {
    byte temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  private static boolean isValidUTF8(byte[] bytes) {
    if ((bytes[3] & 0x80) == 0) {
//      System.out.println("case 1 match");
      return true; // 1-byte UTF-8 character
    } else if ((bytes[3] & 0xE0) == 0xC0 && (bytes[2] & 0xC0) == 0x80) {
//      System.out.println("case 2 match");
      return true; // 2-byte UTF-8 character
    } else if ((bytes[3] & 0xF0) == 0xE0 && (bytes[2] & 0xC0) == 0x80
        && (bytes[1] & 0xC0) == 0x80) {
//      System.out.println("case 3 match");
      return true; // 3-byte UTF-8 character
    } else if ((bytes[3] & 0xF8) == 0xF0 && (bytes[2] & 0xC0) == 0x80 && (bytes[1] & 0xC0) == 0x80
        && (bytes[0] & 0xC0) == 0x80) {
//      System.out.println("case 4 match");
      return true; // 4-byte UTF-8 character
    }
    return false; // Invalid UTF-8 byte sequence
  }
}