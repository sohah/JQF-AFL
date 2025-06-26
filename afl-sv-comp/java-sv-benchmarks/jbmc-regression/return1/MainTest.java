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
 *     license: 4-clause BSD (see /java/jbmc-regression/LICENSE)
 *     repo: https://github.com/diffblue/cbmc.git
 *     branch: develop
 *     directory: regression/cbmc-java/return1
 * The benchmark was taken from the repo: 24 January 2018
 */
@RunWith(JQF.class)
public class MainTest {
  public static short short_value() {
    short s = 1;
    return s;
  }

  public static boolean bool_value() {
    boolean b = true;
    return b;
  }

  public static long long_value() {
    long l = 0x10000ffffl;
    return l;
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    short s = short_value();
    if (s == 1) if (bool_value()) if (long_value() == 0x10000ffffl) assert false;
  }
}
