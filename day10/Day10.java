import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Day10 {
  public static void main(String[] args) throws IOException {
//    solve("day10/square.txt");
//    solve("day10/complex.txt");
//    solve("day10/larger.txt");
//    solve("day10/larger-outside.txt");
    solve("day10/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    String[][] grid = new String[lines.size()][lines.get(0).split("").length];
    P start = null;
    for (int i = 0; i < lines.size(); i++) {
      String[] row = lines.get(i).split("");
      for (int j = 0; j < row.length; j++) {
        grid[i][j] = row[j];
        if (row[j].equals("S")) {
          start = new P(i, j);
        }
      }
    }

    System.out.println(STR. "Start is \{ start }" );
    printGrid(grid);
    List<P> path = walk(grid, start);
    System.out.println(STR. "Path len = \{ path.size() }. Ans = \{ path.size() / 2 }" );

    P leftMost = path.stream().filter(p -> {
      String value = value(grid, p);
      return value.equals("F") || value.equals("|") || value.equals("L");
    }).sorted(Comparator.comparing(P::i)).findFirst().orElseThrow(() -> new IllegalStateException("No leftmost"));

    System.out.println(STR. "Leftmost \{ leftMost }" );


    // Cheated, I know....
    java.awt.Polygon poly = new Polygon();
    for (P p : path) {
      poly.addPoint(p.i, p.j);
    }
    Set<P> inside = new HashSet<>();
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[0].length; j++) {
        P p = new P(i, j);
        if (poly.contains(i, j) && !path.contains(p)) {
          inside.add(p);
        }
      }
    }

    System.out.println(STR."Inside \{inside}: count = \{inside.size()}");
  }

  private static List<P> walk(String[][] grid, P start) {
    var visited = new ArrayList<P>();
    var stack = new Stack<P>();
    stack.push(start);

    while (!stack.isEmpty()) {
      var curr = stack.pop();
      var currVal = value(grid, curr);
      if (!visited.contains(curr)) {
        System.out.println(STR. "entering \{ curr }: \{ currVal }" );
        visited.add(curr);
      }

      for (var p : neighbors(grid, curr)) {
        var nVal = value(grid, p);
        if (!visited.contains(p) && !nVal.equals(".")) {
          stack.push(p);
        }
      }
    }

    System.out.println("BAD");
    return visited;
  }

  private static String value(String[][] grid, P p) {
    return grid[p.i][p.j];
  }

  private static List<P> neighbors(String[][] grid, P current) {
    var north = new P(current.i - 1, current.j);
    var south = new P(current.i + 1, current.j);
    var west = new P(current.i, current.j - 1);
    var east = new P(current.i, current.j + 1);
    List<P> neigbours = switch (grid[current.i][current.j]) {
      case "S" -> List.of(north, south, west, east);
      case "F" -> List.of(south, east);
      case "L" -> List.of(north, east);
      case "J" -> List.of(north, west);
      case "7" -> List.of(south, west);
      case "-" -> List.of(west, east);
      case "|" -> List.of(north, south);
      default -> throw new IllegalStateException();
    };

    return neigbours.stream()
        .filter(p -> insideGrid(grid, p)).toList();
  }

  private static boolean insideGrid(String[][] grid, P p) {
    if (p.i >= 0 && p.i < grid.length && p.j >= 0 && p.j < grid[0].length) {
      return true;
    }
    return false;
  }

  private static void printGrid(String[][] grid) {
    Arrays.stream(grid).forEach(i -> {
      Arrays.stream(i)
          .forEach(j -> System.out.print(j + ""));
      System.out.println();
    });
  }

  record P(int i, int j) {
    @Override
    public String toString() {
      return STR. "(\{ i }, \{ j })" ;
    }
  }
}
