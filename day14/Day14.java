import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day14 {
  public static void main(String[] args) throws IOException {
//    solve("day14/sample.txt");
    solve("day14/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    List<String> tLines = transpose(lines);

    List<String> reposition = reposition(tLines);
    int res = 0;
    for (String r : reposition) {
      for (int i = 1; i <= r.length(); i++) {
        if (r.charAt(i - 1) == 'O') res += r.length() - i + 1;
      }
    }
    System.out.println(STR."Res = \{res}");
  }

  private static List<String> reposition(List<String> tLines) {
    List<String> repositioned = new ArrayList<>();
    for (String line : tLines) {
      String[] split = line.split("");

      for (int i = 0; i < split.length; i++) {
        for (int j = 0; j < split.length - i - 1; j++) {
          if (split[j].equals(".") && split[j + 1].equals("O")) {
            var temp = split[j];
            split[j] = split[j + 1];
            split[j + 1] = temp;
          }
        }
      }
      repositioned.add(String.join("", split));
    }
    return repositioned;
  }

  private static List<String> transpose(List<String> lines) {
    var res = new ArrayList<String>();
    for (int i = 0; i < lines.get(0).length(); i++) {
      String transposedRow = "";
      for (String line : lines) {
        transposedRow += line.charAt(i);
      }
      res.add(transposedRow);
    }

    return res;
  }
}
