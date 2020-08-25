import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
import org.specmath.library.UnexpectedTypeException;
import org.specmath.library.UnionConflictException;
import org.specmath.library.UnionOptions;
import picocli.CommandLine;

@ExtendWith(MockitoExtension.class)
class UnionCommandTest {
  final PrintStream originalOut = System.out;
  final PrintStream originalErr = System.err;
  final ByteArrayOutputStream out = new ByteArrayOutputStream();
  final ByteArrayOutputStream err = new ByteArrayOutputStream();

  @Mock private SpecMathService specMath;

  @Spy @InjectMocks private UnionCommand unionCommand;

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
  public void union_withoutException_succeeds()
      throws UnionConflictException, UnexpectedTypeException, IOException,
          AllUnmatchedFilterException {
    when(specMath.union(anyList(), any(UnionOptions.class))).thenReturn("");

    String[] args =
        "-o src/test/resources/fakeFileName.yaml src/test/resources/emptyFile src/test/resources/emptyFile"
            .split(" ");

    new CommandLine(unionCommand).execute(args);

    assertThat(err.toString()).isEqualTo("");
    assertThat(out.toString())
        .isEqualTo(
            "The union operation succeeded. Result file written to src/test/resources/fakeFileName.yaml.\n");
    verify(unionCommand, times(1)).writeResultToDisk(anyString());
  }

  @Test
  public void union_withException_displaysErrorMessage()
      throws UnionConflictException, UnexpectedTypeException, IOException,
          AllUnmatchedFilterException {
    when(specMath.union(anyList(), any(UnionOptions.class)))
        .thenThrow(UnionConflictException.class);

    String[] args =
        "-o src/test/resources/fakeFileName.yaml src/test/resources/emptyFile src/test/resources/emptyFile"
            .split(" ");

    new CommandLine(unionCommand).execute(args);

    assertThat(err.toString()).isEqualTo("");
    assertThat(out.toString())
        .isEqualTo(Files.readString(Path.of("src/test/resources/expectedOutputUnionConflict.txt")));
    verify(unionCommand, times(1)).writeConflictFileToDisk(any(UnionConflictException.class));
  }
}
