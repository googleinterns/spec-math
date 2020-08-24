import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.specmath.library.SpecMath;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "overlay",
    description = "Perform the overlay operation.",
    mixinStandardHelpOptions = true)
class OverlayCommand implements Callable<Integer> {
  @Option(
      names = {"--output"},
      required = true,
      description = "name of the output file")
  String outputFilename;

  @Option(
      names = {"--overlay"},
      required = true,
      description = "path to the overlay file")
  Path overlayFilePath;

  @Parameters(arity = "1")
  private Path file;

  public static void main(String[] args) {
    int exitCode =
        new CommandLine(new OverlayCommand())
            .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
            .execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    String overlay = "";

    if (overlayFilePath != null) {
      overlay = Files.readString(overlayFilePath);
    }

    String specString = Files.readString(file);

    String result = SpecMath.applyOverlay(overlay, specString);
    Files.writeString(Paths.get(outputFilename), result);
    System.out.printf(
        "The overlay operation succeeded. Result file written to %s.\n", outputFilename);

    return 0;
  }
}