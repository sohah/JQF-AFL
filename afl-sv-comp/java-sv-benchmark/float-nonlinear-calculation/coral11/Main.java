// This file is part of the SV-Benchmarks collection of verification tasks:
// https://gitlab.com/sosy-lab/benchmarking/sv-benchmarks
//
// SPDX-FileCopyrightText: 2024 The SV-Benchmarks Community
//
// SPDX-License-Identifier: Apache-2.0

import org.sosy_lab.sv_benchmarks.Verifier;

public class Main {
  public static void main(String[] main) {
    double d1 = Verifier.nondetDouble();
    System.out.println("d1=" + d1);
    double d2 = Verifier.nondetDouble();
    System.out.println("d2=" + d2);
    double d3 = Verifier.nondetDouble();
    System.out.println("d3=" + d3);
    double d4 = Verifier.nondetDouble();
    System.out.println("d4=" + d4);
    JPFBenchmark.benchmark11(d1, d2, d3, d4);
  }
}
