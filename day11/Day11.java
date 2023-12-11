import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day11 {
  public static final int EXPANSION_RATE = 1_000_000;

  public static void main(String[] args) throws IOException {
//    solve("day11/sample.txt");
    solve("day11/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    Set<Integer> emptyLines = new HashSet<>();
    Set<Integer> emptyCols = new HashSet<>();
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (!line.contains("#")) {
        emptyLines.add(i);
      }
    }

    for (int i = 0; i < lines.get(0).length(); i++) {
      boolean containsGalaxy = false;
      for (int j = 0; j < lines.size(); j++) {
        String line = lines.get(j);
        if (line.charAt(i) == '#') {
          containsGalaxy = true;
          break;
        }
      }

      if (!containsGalaxy) {
        emptyCols.add(i);
      }
    }

    List<P> galaxies = new ArrayList<>();
    for (int i = 0; i < lines.size(); i++) {
      for (int j = 0; j < lines.get(0).length(); j++) {
        if (lines.get(i).charAt(j) == '#') {
          galaxies.add(new P(i, j));
        }
      }
    }

    long sum = 0;
    for (int i = 0; i < galaxies.size() - 1; i++) {
      for (int j = i + 1; j < galaxies.size(); j++) {
        P a = galaxies.get(i);
        P b = galaxies.get(j);

        if (a == b) {
          throw new IllegalStateException();
        }

        long rowDiff = Math.abs(a.i - b.i);
        long colDiff = Math.abs(a.j - b.j);

        for (int k = Math.min(a.i, b.i); k < Math.max(a.i, b.i); k++) {
          if (emptyLines.contains(k)) {
            rowDiff += (EXPANSION_RATE - 1);
          }
        }

        for (int k = Math.min(a.j, b.j); k < Math.max(a.j, b.j); k++) {
          if (emptyCols.contains(k)) {
            colDiff += (EXPANSION_RATE - 1);
          }
        }

        long dist = rowDiff + colDiff;
        sum += dist;
      }
    }

    System.out.println(STR. "Res: \{ sum }" );
  }

  record P(int i, int j) {
  }
}
