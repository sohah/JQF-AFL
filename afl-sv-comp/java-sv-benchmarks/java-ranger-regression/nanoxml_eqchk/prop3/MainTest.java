import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.sosy_lab.sv_benchmarks.Verifier;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.junit.runner.RunWith;

import org.sosy_lab.sv_benchmarks.Verifier;

@RunWith(JQF.class)
public class MainTest {

    @Fuzz
    public void mainTest(InputStream input) throws IOException {
    Verifier.input = input;
    char c0 = Verifier.nondetChar();
    char c1 = Verifier.nondetChar();
    char c2 = Verifier.nondetChar();
    char c3 = Verifier.nondetChar();
    char c4 = Verifier.nondetChar();
    char c5 = Verifier.nondetChar();
    char c6 = Verifier.nondetChar();
    char c7 = Verifier.nondetChar();
    char c8 = Verifier.nondetChar();
    DumpXML d = new DumpXML();
    int out1 = d.mainProcess(c0, c1, c2, c3, c4, c5, c6, c7, c8);
    int out2 = d.mainProcess(c0, c1, c2, c3, c4, c5, c6, c7, c8);
    checkEquality(out1, out2);
  }

  public static void checkEquality(int outSPF, int outJR) {
    if (outSPF == outJR) System.out.println("Match");
    else {
      System.out.println("Mismatch");
      assert (false);
    }
  }
}
