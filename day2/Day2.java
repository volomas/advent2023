import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day2 {
  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    Files.lines(path).forEach(System.out::println);
  }
}
