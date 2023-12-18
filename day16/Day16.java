import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day16 {

  public static void main(String[] args) throws IOException {
//    solve("day16/sample.txt");
    solve("day16/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    System.out.println(lines);
    int most = 0;
    for (int i = 0; i < lines.size(); i++) {
      var fromLeft = new P(i, 0);
      var fromRight = new P(i, lines.get(0).length() - 1);
      int e1 = traceBeam(lines, fromLeft, Dir.RIGHT);
      int e2 = traceBeam(lines, fromRight, Dir.LEFT);
      if (most < e1) {
        most = e1;
      }

      if (most < e2) {
        most = e2;
      }
    }

    for (int j = 1; j < lines.get(0).length() - 1; j++) {
      var fromUp = new P(0, j);
      var fromDown = new P(lines.size() - 1, j);
      int e1 = traceBeam(lines, fromDown, Dir.UP);
      int e2 = traceBeam(lines, fromUp, Dir.DOWN);
      if (most < e1) {
        most = e1;
      }

      if (most < e2) {
        most = e2;
      }
    }
    System.out.println(STR. "Most energized: \{ most }" );
  }

  enum Dir {UP, DOWN, LEFT, RIGHT}


  private static int traceBeam(List<String> lines, P curr, Dir startingDir) {
    var visited = new HashSet<PDir>();
    var energized = new HashSet<P>();
    var stack = new ArrayDeque<Map.Entry<P, Dir>>();
    stack.push(Map.entry(curr, startingDir));

    while (!stack.isEmpty()) {
      Map.Entry<P, Dir> entry = stack.pop();
      P point = entry.getKey();
      Dir d = entry.getValue();
      int i = point.i;
      int j = point.j;
      if (i < 0 || i >= lines.size() || j < 0 || j >= lines.get(0).length() || visited.contains(new PDir(point, d))) {
        continue;
      }

      energized.add(point);
      visited.add(new PDir(point, d));
      char current = lines.get(i).charAt(j);
      switch (current) {
        case '\\' -> {
          switch (d) {
            case UP -> stack.push(Map.entry(nextP(point.i, point.j, Dir.LEFT), Dir.LEFT));
            case DOWN -> stack.push(Map.entry(nextP(point.i, point.j, Dir.RIGHT), Dir.RIGHT));
            case LEFT -> stack.push(Map.entry(nextP(point.i, point.j, Dir.UP), Dir.UP));
            case RIGHT -> stack.push(Map.entry(nextP(point.i, point.j, Dir.DOWN), Dir.DOWN));
          }
        }
        case '/' -> {
          switch (d) {
            case UP -> stack.push(Map.entry(nextP(point.i, point.j, Dir.RIGHT), Dir.RIGHT));
            case DOWN -> stack.push(Map.entry(nextP(point.i, point.j, Dir.LEFT), Dir.LEFT));
            case LEFT -> stack.push(Map.entry(nextP(point.i, point.j, Dir.DOWN), Dir.DOWN));
            case RIGHT -> stack.push(Map.entry(nextP(point.i, point.j, Dir.UP), Dir.UP));
          }
        }
        case '|' -> {
          switch (d) {
            case LEFT, RIGHT -> {
              stack.push(Map.entry(nextP(point.i, point.j, Dir.UP), Dir.UP));
              stack.push(Map.entry(nextP(point.i, point.j, Dir.DOWN), Dir.DOWN));
            }
            case UP, DOWN -> stack.push(Map.entry(nextP(point.i, point.j, d), d));
          }
        }
        case '-' -> {
          switch (d) {
            case UP, DOWN -> {
              stack.push(Map.entry(nextP(point.i, point.j, Dir.RIGHT), Dir.RIGHT));
              stack.push(Map.entry(nextP(point.i, point.j, Dir.LEFT), Dir.LEFT));
            }
            case LEFT, RIGHT -> stack.push(Map.entry(nextP(point.i, point.j, d), d));
          }
        }
        case '.' -> stack.push(Map.entry(nextP(point.i, point.j, d), d));
        default -> throw new IllegalStateException();
      }
//      print(lines.size(), lines.get(0).length(), energized);
    }
    return energized.size();
  }

  private static void print(int n, int m, Set<P> energized) {
    System.out.println("------");
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        if (energized.contains(new P(i, j))) {
          System.out.print("#");
        } else {
          System.out.print(".");
        }
      }
      System.out.println();
    }

  }

  private static P nextP(int i, int j, Dir direction) {
    return switch (direction) {
      case UP -> new P(i - 1, j);
      case DOWN -> new P(i + 1, j);
      case LEFT -> new P(i, j - 1);
      case RIGHT -> new P(i, j + 1);
    };
  }

  record P(int i, int j) {
  }

  record PDir(P p, Dir d) {
  }
}
