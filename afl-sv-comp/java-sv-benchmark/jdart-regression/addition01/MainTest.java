import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

/* Copyright, TU Dortmund 2020 Malte Mues
 * contributed-by: Malte Mues (mail.mues@gmail.com)
 *
 * This benchmark task is a modificaiton of the following original Benchmark:
 * Origin of the benchmark:
 *     license: MIT (see /java/jayhorn-recursive/LICENSE)
 *     repo: https://github.com/jayhorn/cav_experiments.git
 *     branch: master
 *     root directory: benchmarks/recursive
 * The benchmark was taken from the repo: 24 January 2018
 *
 * Following the original license model, modifications are as well licensed  under the
 * MIT license.
 */

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

  public static int addition(int m, int n, int c) {
    if (n == 0) {
      return m;
    }

    if (c >= 150) {
      assert false;
    }

    if (n > 0) {
      return addition(m + 1, n - 1, ++c);
    } else {
      return addition(m - 1, n + 1, ++c);
    }
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    int m = Verifier.nondetInt();
    if (m < 0 || m >= 10000) {
      return;
    }
    int n = Verifier.nondetInt();
    if (n < 0 || n >= 10000) {
      return;
    }
    int c = 0;
    int result = addition(m, n, c);
    assert (result == m + n);
  }
}
