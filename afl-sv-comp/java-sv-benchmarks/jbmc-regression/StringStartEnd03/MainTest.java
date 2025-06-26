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
 *     directory: regression/jbmc-strings/StringStartEnd03
 * The benchmark was taken from the repo: 24 January 2018
 */
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    String[] strings = new String[4];
    strings[0] = Verifier.nondetString();
    strings[1] = Verifier.nondetString();
    strings[2] = Verifier.nondetString();
    strings[3] = Verifier.nondetString();

    int i = 0;
    for (String string : strings) {
      if (string.endsWith("ed")) ++i;
    }
    assert i == 3;
  }
}
