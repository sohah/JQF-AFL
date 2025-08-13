/*
 * Contributed to SV-COMP by Falk Howar
 * License: MIT (see /java/jdart-regression/LICENSE-MIT)
 *
 */

import org.sosy_lab.sv_benchmarks.Verifier;

public class Main {

  public static void main(String[] args) {

    int i = Verifier.nondetInt();
    System.out.println("i=" + i);

    if (i < 1 || i > 100) {
      return;
    }

    assert ((1 << i) != 1);
  }
}
