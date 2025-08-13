import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks
//
// SPDX-FileCopyrightText: 2011-2013 Alexander von Rhein, University of Passau
// SPDX-FileCopyrightText: 2011-2021 The SV-Benchmarks Community
//
// SPDX-License-Identifier: Apache-2.0

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

  private static int cleanupTimeShifts = 2;

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    randomSequenceOfActions(3);
  }

  public static boolean getBoolean() throws IOException {
    return Verifier.nondetBoolean();
  }

  public static void randomSequenceOfActions(int maxLength) throws IOException {
    Actions a = new Actions();

    int counter = 0;
    while (counter < maxLength) {
      counter++;

      boolean action1 = getBoolean();
      boolean action2 = getBoolean();
      boolean action3 = getBoolean();
      boolean action4 = false;
      if (!action3) action4 = getBoolean();

      if (action1) {
        a.waterRise();
      }

      if (action2) {
        a.methaneChange();
      }

      if (action3) {
        a.startSystem();
      } else if (action4) {
        a.stopSystem();
      }

      a.timeShift();
    }

    for (counter = 0; counter < cleanupTimeShifts; counter++) {
      a.timeShift();
    }
  }
}
