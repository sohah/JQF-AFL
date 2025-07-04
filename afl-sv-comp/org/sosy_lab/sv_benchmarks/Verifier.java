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
    int b1 = input.read();
    int b2 = input.read();

    if (b1 == -1 || b2 == -1) {
      throw new EOFException("Not enough bytes to read a char");
    }
    char c = (char) ((b1 << 8) | (b2 & 0xFF));
    System.out.println("Generated char: " + c);
    return c;
  }

  public static boolean nondetBoolean() throws IOException {
    int b = input.read();
    if (b == -1)
      throw new EOFException("Not enough bytes available in input stream to read an int");
    boolean outputBoolean = (b & 1) == 1;
    System.out.println("Generated boolean: " + outputBoolean);
    return outputBoolean;
  }

  public static short nondetShort() throws IOException {
    int i = 0;
    for (int j = 0; j < 2; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      i = (i << 8) | b;
    }
    System.out.println("Generated short: " + i);
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
    System.out.println("Generated int: " + i);
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
    System.out.println("Generated long: " + l);
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
    float outputFloat = Float.intBitsToFloat(bits);
    System.out.println("Generated float: " + outputFloat);
    return outputFloat;
  }

  public static double nondetDouble() throws IOException {
    long bits = 0;
    for (int j = 0; j < 8; j++) {
      int b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
      bits = (bits << 8) | (b & 0xFF);
    }
    double outputDouble = Double.longBitsToDouble(bits);
    System.out.println("Generated double: " + outputDouble);
    return outputDouble;
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
      byte[] utf8Char = readUTF8Char();
      outputStr.append(new String(utf8Char, StandardCharsets.UTF_8));
      b = input.read();
      if (b == -1)
        throw new EOFException("Not enough bytes available in input stream to read an int");
    }
    System.out.println("Final nondetString = " + outputStr);
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
      return new byte[]{bytes[0], bytes[1], bytes[2]}; // 3-byte UTF-8 character
    } else if ((bytes[0] & 0xF8) == 0xF0 && (bytes[1] & 0xC0) == 0x80 && (bytes[2] & 0xC0) == 0x80
        && (bytes[3] & 0xC0) == 0x80) {
      return new byte[]{bytes[0],bytes[1],bytes[2],bytes[3]}; // 4-byte UTF-8 character
    }
    return null; // Invalid UTF-8 byte sequence
  }
}