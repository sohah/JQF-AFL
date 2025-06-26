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
    MathSin sin = new MathSin();
    double d = Verifier.nondetDouble();
    assert sin.mysin(d) < 0.5;
  }
}
