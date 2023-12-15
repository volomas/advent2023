import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day15 {
  public static void main(String[] args) throws IOException {
//    solve("day15/sample.txt");
    solve("day15/input.txt");
  }

  private static void solve(String filename) throws IOException {
    String input = Files.readString(Path.of(filename)).replaceAll("\n", "");
    int sum = 0;
    for (String seq : input.split(",")) {
      System.out.println(STR."Hash of \{seq} is \{hash(seq)}");
      sum += hash(seq);
    }
    System.out.println(STR."Result = \{sum}");
  }

  private static int hash(String seq) {
    int h = 0;
    for (char c : seq.toCharArray()) {
      h += c;
      h *= 17;
      h %= 256;
    }
    return h;
  }
}
