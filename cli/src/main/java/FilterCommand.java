import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;
import org.specmath.library.FilterOptions;
import org.specmath.library.SpecMath;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "filter",
    description = "Perform the filter operation.",
    mixinStandardHelpOptions = true)
class FilterCommand implements Callable<Integer> {
  @Option(
      names = {"-o", "--output"},
      required = true,
      description = "name of the output file")
  String outputFilename;

  @Option(
      names = {"-d", "--defaults"},
      required = false,
      description = "path to defaults file")
  Path defaultsPath;

  @Option(
      names = {"-f", "--filterFile"},
      required = true,
      description = "path to the filter file")
  Path filterFilePath;

  @Parameters(arity = "1")
  private Path file;

  public static void main(String[] args) {
    int exitCode =
        new CommandLine(new FilterCommand())
            .setExecutionExceptionHandler(new PrintExceptionMessageHandler())
            .execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    String defaults = "";

    if (defaultsPath != null) {
      defaults = Files.readString(defaultsPath);
    }

    FilterOptions filterOptions =
        FilterOptions.builder().defaults(defaults).build();

    String specString = Files.readString(file);
    String filterFileString = Files.readString(filterFilePath);

    String result = SpecMath.filter(specString, filterFileString, filterOptions);
    Files.writeString(Paths.get(outputFilename), result);
    System.out.printf(
        "The filter operation succeeded. Result file written to %s.\n", outputFilename);

    return 0;
  }
}
