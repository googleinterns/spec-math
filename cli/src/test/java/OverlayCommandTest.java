import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.specmath.library.UnexpectedTypeException;
import picocli.CommandLine;

@ExtendWith(MockitoExtension.class)
class OverlayCommandTest {
  final PrintStream originalOut = System.out;
  final PrintStream originalErr = System.err;
  final ByteArrayOutputStream out = new ByteArrayOutputStream();
  final ByteArrayOutputStream err = new ByteArrayOutputStream();

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
  @Mock private SpecMathService specMath;
  @Spy @InjectMocks private OverlayCommand overlayCommand;

  @BeforeEach
  public void setUpStreams() {
    out.reset();
    err.reset();
    System.setOut(new PrintStream(out));
    System.setErr(new PrintStream(err));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  public void overlay_withoutException_succeeds() throws UnexpectedTypeException, IOException {
    when(specMath.applyOverlay(anyString(), anyString())).thenReturn("");

    String[] args =
        "--output=src/test/resources/fakeFileName.yaml --overlay=src/test/resources/emptyFile src/test/resources/emptyFile"
            .split(" ");

    new CommandLine(overlayCommand).execute(args);

    assertThat(err.toString()).isEqualTo("");
    assertThat(out.toString())
        .isEqualTo(
            "The overlay operation succeeded. Result file written to src/test/resources/fakeFileName.yaml.\n");
    verify(overlayCommand, times(1)).writeResultToDisk(anyString());
  }
}
