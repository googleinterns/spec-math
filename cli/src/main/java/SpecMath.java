import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * The entry point of the CLI. The main purpose is to declare the subcommands under the main
 * command.
 */
@Command(
    subcommands = {UnionCommand.class, FilterCommand.class, OverlayCommand.class},
    mixinStandardHelpOptions = true,
    name = "specmath",
    version = "1.0",
    description = "Perform Spec Math operations on OpenAPI Specifications. For more information please see the README.")
public class SpecMath implements Callable<Integer> {
  public static void main(String[] args) {
    Integer exitCode = new CommandLine(new SpecMath()).execute(args);
    System.exit(exitCode);
  }

  @Override
  public Integer call() {
    return 0;
  }
}
