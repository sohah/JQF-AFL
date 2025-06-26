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

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;

    int i = Verifier.nondetInt();
    System.out.println("i=" + i);

    if (i < 1 || i > 100) {
      return;
    }

    assert ((1 << i) != 1);
  }
}
