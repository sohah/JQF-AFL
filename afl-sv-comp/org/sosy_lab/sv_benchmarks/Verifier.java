package org.sosy_lab.sv_benchmarks;

import java.io.*;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.charset.StandardCharsets;

public class Verifier {

  public static InputStream input;

  public static void assume(boolean condition) throws EOFException {
    if (!condition) {
      throw new EOFException("Assume condition failed");
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
    int size = 0;
    while (b >= 26 && size
        < 200) { //generate an invalid utf-8 encoding 10% of the time, i.e., that's 25/256 ≈ 10% of the byte range
      System.out.println("size so far = " + ++size);
      byte[] utf8Char = readUTF8Char();
      System.out.println("outputStr before append = " + outputStr);
      outputStr.append(new String(utf8Char, StandardCharsets.UTF_8));
      System.out.println("outputStr after append = " + outputStr);
      b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
    }
    return outputStr.toString();
  }


  private static byte[] readUTF8Char() throws IOException {
    byte[] fourBytes = new byte[4];
    int numBytesRead = 0;
    do {
      while (numBytesRead < 4) {
        int b = input.read();
        if (b == -1)
          throw new EOFException("Not enough bytes available in input stream to read an int");
        fourBytes[numBytesRead++] = (byte) b;
      }
      byte[] validUTF8 = prefixOfUTF8(fourBytes);
      if (validUTF8!=null)
        return validUTF8;
      else {
        fourBytes[0] = fourBytes[1];
        fourBytes[1] = fourBytes[2];
        fourBytes[2] = fourBytes[3];
        fourBytes[3] = 0;
        numBytesRead--;
      }
    } while (true);
  }

  private static byte[] prefixOfUTF8(byte[] bytes) {
    if ((bytes[0] & 0x80) == 0) {
      return new byte[]{bytes[0]}; // 1-byte UTF-8 character
    } else if ((bytes[0] & 0xE0) == 0xC0 && (bytes[1] & 0xC0) == 0x80) {
      return new byte[]{bytes[0], bytes[1]}; // 2-byte UTF-8 character
    } else if ((bytes[0] & 0xF0) == 0xE0 && (bytes[1] & 0xC0) == 0x80
        && (bytes[2] & 0xC0) == 0x80) {
      return new byte[]{bytes[0], bytes[1], bytes[0]}; // 3-byte UTF-8 character
    } else if ((bytes[0] & 0xF8) == 0xF0 && (bytes[1] & 0xC0) == 0x80 && (bytes[2] & 0xC0) == 0x80
        && (bytes[3] & 0xC0) == 0x80) {
      return new byte[]{bytes[0],bytes[1],bytes[2],bytes[3]}; // 4-byte UTF-8 character
    }
    return null; // Invalid UTF-8 byte sequence
  }
}