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
 *     repo: https://github.com/marek-trtik/cbmc.git
 *     branch: add_string_regression_tests
 *     directory: regression/jbmc-strings-test-gen/BufferedReaderReadLine
 * The benchmark was taken from the repo: 24 January 2018
 */
import java.io.BufferedReader;
import java.io.StringReader;
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

  public static String check(BufferedReader br) throws Exception {
    String s = br.readLine();
    return s;
  }

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    String arg = Verifier.nondetString();
    String thisLine = null;
    int numLines = 0;

    try {
      BufferedReader br = new BufferedReader(new StringReader(arg));
      String line = check(br);
      while ((thisLine = check(br)) != null) {
        System.out.println(thisLine);
        numLines += 1;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    assert numLines > 0;
  }
}
