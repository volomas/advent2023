import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day6 {
  public static void main(String[] args) throws IOException {
    solve("day6/input.txt");
  }

  private static void solve(String filename) throws IOException {
    Path path = Path.of(filename);
    List<String> lines = Files.readAllLines(path);
    long time = Long.parseLong(lines.get(0).split(":\s+")[1].replaceAll("\s+", ""));
    long distance = Long.parseLong(lines.get(1).split(":\s+")[1].replaceAll("\s+", ""));
    long res = winningCount(time, distance);
    System.out.println(STR. "res = \{ res }" );
  }

  private static int winningCount(long time, long distance) {

    double D = Math.pow(time, 2) - (4 * distance);
    var x1 = ((-time) - Math.sqrt(D)) / -2;
    var x2 = ((-time) + Math.sqrt(D)) / -2;

    int from = (int) Math.floor(Math.min(x1, x2) + 1);
    int to = (int) Math.ceil(Math.max(x1, x2) - 1);
    return to - from + 1;
  }
}
