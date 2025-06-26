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
 *     directory: regression/cbmc-java/exceptions12
 * The benchmark was taken from the repo: 24 January 2018
 */
class A extends RuntimeException {}

class B extends A {}

class C extends B {}

class F {
  void foo() {
    try {
      B b = new B();
      throw b;
    } catch (B exc) {
      assert false;
    }
  }
}

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    try {
      F f = new F();
      f.foo();
    } catch (B exc) {
      assert false;
    }
  }
}
