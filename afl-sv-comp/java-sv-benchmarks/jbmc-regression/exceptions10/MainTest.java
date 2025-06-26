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
 *     directory: regression/cbmc-java/exceptions10
 * The benchmark was taken from the repo: 24 January 2018
 */
class A extends RuntimeException {}

class B extends A {}

class C extends B {}

@RunWith(JQF.class)
public class MainTest {
  static void foo() {
    try {
      A b = new A();
      throw b;
    } catch (A exc) {
      assert false;
    }
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    try {
      A a = new A();
      foo();
    } catch (B exc) {
      assert false;
    }
  }
}
