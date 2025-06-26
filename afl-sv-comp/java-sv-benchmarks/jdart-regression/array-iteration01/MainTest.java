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
 * Origin of the benchmark:
 *     license: 4-clause BSD (see /java/jbmc-regression/LICENSE)
 *     repo: https://github.com/diffblue/cbmc.git
 *     branch: develop
 *     directory: regression/cbmc-java/aastore_aaload1
 * The benchmark was taken from the repo: 24 January 2018
 *
 * Following the original license model, modifications are as well licensed  under the
 * MIT license.
 */
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    int[] ia = new int[200];
    int index = Verifier.nondetInt();
    Verifier.assume(0 <= index && index < ia.length);
    ia[index] = Verifier.nondetInt();
    for (int i = 0; i < ia.length; i++) {
      assert ia[i] != 0;
    }
  }
}
