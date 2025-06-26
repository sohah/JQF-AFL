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
 *     directory: regression/jbmc-strings/StringConcatenation03
 * The benchmark was taken from the repo: 24 January 2018
 */
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    String s1 = Verifier.nondetString();
    String s2 = Verifier.nondetString();

    System.out.printf("Result of s1.concat(s2) = %s\n", s1.concat(s2));
    String tmp = s1.concat(s2);
    assert tmp.equals("Happy at DiffBllue");

    tmp = s1;
    System.out.printf("s1 after concatenation = %s\n", s1);
    assert tmp.equals("Happy at");
  }
}
