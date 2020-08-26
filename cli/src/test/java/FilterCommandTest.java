import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import org.specmath.library.AllUnmatchedFilterException;
import org.specmath.library.FilterOptions;
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import picocli.CommandLine;

@ExtendWith(MockitoExtension.class)
class FilterCommandTest {
  final PrintStream originalOut = System.out;
  final PrintStream originalErr = System.err;
  final ByteArrayOutputStream out = new ByteArrayOutputStream();
  final ByteArrayOutputStream err = new ByteArrayOutputStream();

  @Mock private SpecMathService specMath;

  @Spy @InjectMocks private FilterCommand filterCommand;

  @Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

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
  public void filter_withoutException_succeeds()
      throws UnionConflictException, UnexpectedTypeException, IOException,
          AllUnmatchedFilterException {
    when(specMath.filter(anyString(), anyString(), any(FilterOptions.class))).thenReturn("");

    String[] args =
        "-o src/test/resources/fakeFileName.yaml -f src/test/resources/emptyFile src/test/resources/emptyFile"
            .split(" ");

    new CommandLine(filterCommand).execute(args);

    assertThat(err.toString()).isEqualTo("");
    assertThat(out.toString())
        .isEqualTo(
            "The filter operation succeeded. Result file written to src/test/resources/fakeFileName.yaml.\n");
    verify(filterCommand, times(1)).writeResultToDisk(anyString());
  }

  @Test
  public void filter_withException_displaysErrorMessage()
      throws UnionConflictException, UnexpectedTypeException, IOException,
          AllUnmatchedFilterException {
    when(specMath.filter(anyString(), anyString(), any(FilterOptions.class)))
        .thenThrow(AllUnmatchedFilterException.class);

    String[] args =
        "-o src/test/resources/fakeFileName.yaml -f src/test/resources/emptyFile src/test/resources/emptyFile"
            .split(" ");

    new CommandLine(filterCommand).execute(args);

    assertThat(err.toString()).isEqualTo("");
    assertThat(out.toString())
        .isEqualTo("The filter operation failed. The result of the filter was empty.\n");
  }
}
