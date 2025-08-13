import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

// SPDX-FileCopyrightText: Java-Ranger authors 2022
// SPDX-License-Identifier: Apache-2.0
// inspired by the motivating example on page 1085 in Thanassis Avgerinos,
// Alexandre Rebert, Sang Kil Cha, and David Brumley. 2014.
//    Enhancing symbolic execution with veritesting.

// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    String arg = Verifier.nondetString();
    int counter = 0;
    for (int i = 0; i < arg.length(); i++) {
      char myChar = arg.charAt(i);
      if (myChar == 'B') counter++;
    }
    assert (counter != 121);
  }
}
