import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    subcommands = {
      UnionCommand.class,
      FilterCommand.class,
      OverlayCommand.class
    },
    mixinStandardHelpOptions = true,
    name = "specmath",
    description = "Perform operations on OpenAPI Specifications")
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


