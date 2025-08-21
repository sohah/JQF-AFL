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
    WBS wbs = new WBS();
    int PedalPos;
    boolean AutoBrake, Skid;
    for (int i = 0; i < 2; i++) {
      PedalPos = Verifier.nondetInt();
      AutoBrake = Verifier.nondetBoolean();
      Skid = Verifier.nondetBoolean();
      wbs.update(PedalPos, AutoBrake, Skid);
      // This assertion should fail:
      assert ((PedalPos > 0 && PedalPos <= 4)
          ? (wbs.Alt_Pressure > 0 || wbs.Nor_Pressure > 0)
          : true);
    }
  }
}
