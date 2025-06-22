package org.sosy_lab.sv_benchmarks;

import java.io.*;
import com.pholser.junit.quickcheck.internal.GeometricDistribution;


public class Verifier {

  public static InputStream input;

  private static GeometricDistribution geometricDistribution =
      new GeometricDistribution();
  private static SourceOfRandomness random = new SourceOfRandomness(new Radom());

  public static void assume(boolean condition) {
    assert false;
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
    int b = input.read();
    final int MEAN_STRING_LENGTH = 10;
    if (b < 26) { //generate an invalid utf-8 encoding 10% of the time, i.e., that's 26/256 ≈ 10.2% of the byte range

    } else { //generate a valid utf-8 encoding 90% of the time
      int stringLength = geometricDistribution.sampleWithMean(MEAN_STRING_LENGTH, random);
    }
//    geometric distribution with mean of 10
//     200 million range will represent the end of the string, for invalid encoding.
    //bitblashing (eager set conversion), choose randomly, and propagate and see the sequences
//    that can sometimes work for the solvers, but similar to fuzz
//    other types of solvers algorithm, do not have any random guess and check in their algorithm and so the fuzzer would substitute this
//    dpll[t], work by having an algorithm for a particular theory and use the dpll for this algorthim,
//    could look at string theories for the [t] here, and thus the fuzzer can be better.
    byte[] buf = new byte[10];
    int read = input.read(buf);
    return new String(buf, 0, read, java.nio.charset.StandardCharsets.UTF_8);
  }
}