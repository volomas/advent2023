import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Day13 {
  public static void main(String[] args) throws IOException {
    solve("day13/sample.txt");
//    solve("day13/input.txt");
  }

  private static void solve(String filename) throws IOException {
    int totalScore = 0;
    for (String section : Files.readString(Path.of(filename)).split("\n\n")) {
      String[] patterns = section.split("\n");
      int rows = patterns.length;
      int cols = patterns[0].length();

      for (int r = 0; r < rows; r++) {
        int score = horizontalMirrorScore(patterns, r);
        if (score > 0) {
          System.out.println(STR. "Section: \{ section }" );
          System.out.println(STR. "---- mirrors horizontally at ---- \{ r } and \{ r + 1 }: score=\{ score }" );
          totalScore += score;
          break;
        }
      }

      for (int c = 0; c < cols; c++) {
        int score = verticalMirrorScore(patterns, c);
        if (score > 0) {
          System.out.println(STR. "Section: \{ section }" );
          System.out.println(STR. "---- mirrors verticall at ---- \{ c } and \{ c + 1 }: score=\{ score }" );
          totalScore += score;
          break;
        }
      }
    }
    System.out.println(STR."Final score is \{totalScore}");
  }

  private static int verticalMirrorScore(String[] patterns, int col) {
    int foldSize = col + 1;
    String[] a = new String[foldSize];
    String[] b = new String[foldSize];
    for (int r = 0; r < patterns.length; r++) {
      for (int i = 0; i < foldSize; i++) {
        a[i] = (a[i] == null ? "" : a[i]) + patterns[r].charAt(i);
      }
      for (int i = 0; i < foldSize; i++) {
        int pos = foldSize + i;
        if (pos < patterns[r].length()) {
          b[i] = (b[i] == null ? "" : b[i]) + patterns[r].charAt(pos);
        }
      }
    }

    if (b[0] == null) {
      return 0;
    }
    boolean mirror = isMirror(a, b);
    if (!mirror) return 0;
    System.out.println(STR. "Found mirror after colIdx = \{ col }" );
    return col + 1;
  }

  private static int horizontalMirrorScore(String[] patterns, int row) {
    int foldSize = row + 1;
    String[] a = Arrays.copyOfRange(patterns, 0, foldSize);
    String[] b = Arrays.copyOfRange(patterns, foldSize, foldSize * 2);
    if (b[0] == null) {
      return 0;
    }
    boolean mirror = isMirror(a, b);
    if (!mirror) return 0;
    System.out.println(STR. "Found mirror after rowIdx = \{ row }" );
    return 100 * (row + 1);
  }

  private static boolean isMirror(String[] a, String[] b) {
    boolean mirror = true;
    for (int i = 0; i < b.length; i++) {
      if (b[i] == null) {
        break;
      }
      if (!b[i].equals(a[a.length - 1 - i])) {
        mirror = false;
        break;
      }
    }
    return mirror;
  }
}
