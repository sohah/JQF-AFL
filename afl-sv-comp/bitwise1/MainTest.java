/*
 * Origin of the benchmark:
 *     license: 4-clause BSD (see /java/jbmc-regression/LICENSE)
 *     repo: https://github.com/diffblue/cbmc.git
 *     branch: develop
 *     directory: regression/cbmc-java/bitwise1
 * The benchmark was taken from the repo: 24 January 2018
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.Assume;
import org.junit.runner.RunWith;

@RunWith(JQF.class)
public class MainTest {

  static char c;

  @Fuzz /* JQF will generate inputs to this method */
  public void mainTest(InputStream input) throws IOException {
// Read and buffer input first
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] temp = new byte[1024];
    int read;
    while ((read = input.read(temp)) != -1) {
      buffer.write(temp, 0, read);
    }
    byte[] inputBytes = buffer.toByteArray();

    System.err.println("Actual input length: " + inputBytes.length);
    System.err.println("Input hex: " + bytesToHex(inputBytes));

    // Set Verifier.input to a reusable stream
    Verifier.input = new ByteArrayInputStream(inputBytes);
    c = (char) (Verifier.nondetInt() * 2 + 1);
    int i = (c | 2);
    assert (i & 3) == 3;

    String str = null;
    str.length(); // Use the input to avoid unused variable warning
  }


  public static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02X", b));
    }
    return sb.toString();
  }
}
