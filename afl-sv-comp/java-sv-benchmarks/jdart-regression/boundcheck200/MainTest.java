import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

/*
 * Contributed to SV-COMP by Falk Howar
 * License: MIT (see /java/jdart-regression/LICENSE-MIT)
 *
 */

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

  private static void recursion(int i) {
    if (i == 0) {
      return;
    }
    if (i > 0) {
      recursion(i - 1);
    }
    if (i < 0) {
      assert false;
    }
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    int x = Verifier.nondetInt();
    if (x < 200 || x > 200) {
      return;
    }

    recursion(x);

    assert false;
  }
}
