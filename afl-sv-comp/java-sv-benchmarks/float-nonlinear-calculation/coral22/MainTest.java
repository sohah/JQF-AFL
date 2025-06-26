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
// SPDX-FileCopyrightText: 2024 The SV-Benchmarks Community
//
// SPDX-License-Identifier: Apache-2.0

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    double d1 = Verifier.nondetDouble();
    System.out.println("d1=" + d1);
    double d2 = Verifier.nondetDouble();
    System.out.println("d2=" + d2);
    double d3 = Verifier.nondetDouble();
    System.out.println("d3=" + d3);
    double d4 = Verifier.nondetDouble();
    System.out.println("d4=" + d4);
    double d5 = Verifier.nondetDouble();
    System.out.println("d5=" + d5);
    double d6 = Verifier.nondetDouble();
    System.out.println("d6=" + d6);
    double d7 = Verifier.nondetDouble();
    System.out.println("d7=" + d7);
    JPFBenchmark.benchmark22(d1, d2, d3, d4, d5, d6, d7);
  }
}
