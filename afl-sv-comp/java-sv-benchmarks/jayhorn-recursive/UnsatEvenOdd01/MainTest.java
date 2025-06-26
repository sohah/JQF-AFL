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
 * Origin of the benchmark:
 *     license: MIT (see /java/jayhorn-recursive/LICENSE)
 *     repo: https://github.com/jayhorn/cav_experiments.git
 *     branch: master
 *     root directory: benchmarks/recursive
 * The benchmark was taken from the repo: 24 January 2018
 */
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

  static int isOdd(int n) {
    if (n == 0) {
      return 0;
    } else if (n == 1) {
      return 1;
    } else {
      return isEven(n - 1);
    }
  }

  static int isEven(int n) {
    if (n == 0) {
      return 1;
    } else if (n == 1) {
      return 0;
    } else {
      return isOdd(n - 1);
    }
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    int n = Verifier.nondetInt();
    if (n < 0) {
      return;
    }
    int result = isEven(n);
    int mod = n % 2;
    if (result < 0 || result == mod) {
      return;
    } else {
      assert false;
    }
  }
}
