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
 *     directory: regression/jbmc-strings/StringBuilderAppend02
 * The benchmark was taken from the repo: 24 January 2018
 */
import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {
    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    Object objectRef = Verifier.nondetString();
    System.out.println("objectRef=" + objectRef);
    String string = Verifier.nondetString();
    System.out.println("string=" + string);
    char[] charArray = {
      'v', 'e', 'r', 'i', 'f', 'i',
      'c', 'a', 't', 'i', 'o', 'n'
    };
    boolean booleanValue = true;
    char characterValue = 'Z';
    int integerValue = 7;
    long longValue = 10000000000L;
    float floatValue = 2.5f;
    double doubleValue = 33.333;

    StringBuilder lastBuffer = new StringBuilder("last buffer");
    StringBuilder buffer = new StringBuilder();

    buffer
        .append(objectRef)
        .append("%n")
        .append(string)
        .append("%n")
        .append(charArray)
        .append("%n")
        .append(charArray, 0, 3)
        .append("%n")
        .append(booleanValue)
        .append("%n")
        .append(characterValue)
        .append("%n")
        .append(integerValue)
        .append("%n")
        .append(longValue)
        .append("%n")
        .append(floatValue)
        .append("%n")
        .append(doubleValue)
        .append("%n")
        .append(lastBuffer);

    String tmp = buffer.toString();
    assert tmp.equals(
        "diffblue%ntest%nverification%nver%ntrue%n%Z%n7%n10000000000%n2.5%n33.333%nlast buffer");
  }
}
