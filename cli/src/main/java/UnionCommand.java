import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import org.specmath.library.UnionConflictException;
import org.specmath.library.UnionOptions;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(
    name = "union",
    description = "Perform the union operation",
    version = "1.0",
    mixinStandardHelpOptions = true)
class UnionCommand implements Callable<Integer> {
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
      names = {"-c", "--conflictResolutions"},
      required = false,
      description = "path to conflict resolutions file")
  Path conflictResolutionsPath;

  @Parameters(
      arity = "2..*",
      paramLabel = "FILEPATH",
      description = "two or more paths to specs to merge")
  private List<Path> filePathsOfSpecsToMerge;

  SpecMathService specMath = new SpecMathService();

  public static void main(String[] args) {
    int exitCode = new CommandLine(new UnionCommand()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() throws Exception {
    List<String> specStrings = new ArrayList<>();
    for (Path path : filePathsOfSpecsToMerge) {
      specStrings.add(Files.readString(path));
    }

    String defaults = "";
    String conflictResolutions = "";

    if (defaultsPath != null) {
      defaults = Files.readString(defaultsPath);
    }
    if (conflictResolutionsPath != null) {
      conflictResolutions = Files.readString(conflictResolutionsPath);
    }

    UnionOptions unionOptions =
        UnionOptions.builder().defaults(defaults).conflictResolutions(conflictResolutions).build();

    try {
      String result = specMath.union(specStrings, unionOptions);
      writeResultToDisk(result);

      System.out.printf(
          "The union operation succeeded. Result file written to %s.\n", outputFilename);
    } catch (UnionConflictException e) {
      handleUnionConflictException(e);
    }

    return 0;
  }

  void writeResultToDisk(String result) throws IOException {
    Files.writeString(Paths.get(outputFilename), result);
  }

  private void handleUnionConflictException(UnionConflictException e) throws IOException {
    System.out.println("There were conflicts in the union process\n");

    String conflictsFilename = writeConflictFileToDisk(e);

    System.out.println("Please see the file " + conflictsFilename + " for more information\n");

    System.out.println("To resolve the conflicts you have three choices:");
    System.out.printf(
        "1: Updating the \"resolvedValue\" property in %s"
            + " with your desired option, and then passing that file into the union as an"
            + " additional parameter using the -c flag\n\n",
        conflictsFilename);
    System.out.printf(
        "2: Looking at %s and manually updating the input YAML files\n\n",
        conflictsFilename);
    System.out.printf(
        "3: Updating/creating a defaults file with "
            + "overrides for the conflicting keypaths in %s, and passing in the new defaults file"
            + " into the union as an additional parameter using the -d flag\n\n",
        conflictsFilename);
  }

  String writeConflictFileToDisk(UnionConflictException e) throws IOException {
    String conflictsFilename = outputFilename + "_CONFLICTS";

    ObjectMapper mapper = new ObjectMapper();

    mapper
        .writerWithDefaultPrettyPrinter()
        .writeValue(new File(conflictsFilename), e.getConflicts());

    return conflictsFilename;
  }
}
