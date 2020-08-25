import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.specmath.library.AllUnmatchedFilterException;
import org.specmath.library.FilterOptions;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "filter",
    description = "Perform the filter operation",
    version = "1.0",
    mixinStandardHelpOptions = true)
class FilterCommand implements Callable<Integer> {
  @Option(
      names = {"-o", "--output"},
      required = true,
      description = "name of the output file")
  String outputFilename;

  @Option(
      names = {"-d", "--defaults", "--overlay"},
      required = false,
      description = "path to defaults file")
  Path defaultsPath;

  @Option(
      names = {"-f", "--filterFile"},
      required = true,
      description = "path to the filter file")
  Path filterFilePath;

  @Parameters(arity = "1", description = "path to the spec file")
  private Path specFilePath;

  SpecMathService specMath = new SpecMathService();

  public static void main(String[] args) {
    int exitCode = new CommandLine(new FilterCommand()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    String defaults = "";

    if (defaultsPath != null) {
      defaults = Files.readString(defaultsPath);
    }

    FilterOptions filterOptions = FilterOptions.builder().defaults(defaults).build();

    String specString = Files.readString(specFilePath);
    String filterFileString = Files.readString(filterFilePath);

    try {
      String result = specMath.filter(specString, filterFileString, filterOptions);
      writeResultToDisk(result);
      System.out.printf(
          "The filter operation succeeded. Result file written to %s.\n", outputFilename);
    } catch (AllUnmatchedFilterException e) {
      System.out.println("The filter operation failed. The result of the filter was empty.");
    }

    return 0;
  }

  void writeResultToDisk(String result) throws IOException {
    Files.writeString(Paths.get(outputFilename), result);
  }
}
