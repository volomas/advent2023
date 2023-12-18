import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day18 {
  public static void main(String[] args) throws IOException {
//    solve("day18/sample.txt");
    solve("day18/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    List<Coord> list = new ArrayList<>();
    list.add(new Coord(0, 0, null));
    for (int i = 0; i < lines.size(); i++) {
      String[] split = lines.get(i).split("\\s+");
      String direction = split[0];
      int depth = Integer.parseInt(split[1]);
      String color = split[2];
      var last = list.getLast();
      switch (direction) {
        case "R" -> {
          for (int k = last.j() + 1; k <= last.j() + depth; k++) {
            list.add(new Coord(last.i(), k, color));
          }
        }
        case "L" -> {
          for (int k = last.j() - 1; k > last.j() - 1 - depth; k--) {
            list.add(new Coord(last.i(), k, color));
          }
        }
        case "D" -> {
          for (int k = last.i() + 1; k <= last.i() + depth; k++) {
            list.add(new Coord(k, last.j(), color));
          }
        }
        case "U" -> {
          for (int k = last.i() - 1; k > last.i() - 1 - depth; k--) {
            list.add(new Coord(k, last.j(), color));
          }
        }
      }
    }
    var poly = new Polygon();
    Set<String> rim = new HashSet<>();
    for (Coord c : list) {
      rim.add(STR."\{c.i}_\{c.j}");
      poly.addPoint(c.j, c.i);
    }
    Rectangle bounds = poly.getBounds();
    int minI = (int) bounds.getMinY();
    int maxI = (int) bounds.getMaxY();
    int minJ = (int) bounds.getMinX();
    int maxJ = (int) bounds.getMaxX();
    int inner = 0;
    for (int i = minI; i <= maxI; i++) {
      for (int j = minJ; j <= maxJ; j++) {
        if (rim.contains(STR."\{i}_\{j}")) {
          continue;
        }
        if (poly.contains(j, i)) {
          inner += 1;
        }
      }
    }
    System.out.println(STR."Rim count: \{rim.size()}");
    System.out.println(STR."Inner count: \{inner}");
    System.out.println(STR."Sum from poly: \{rim.size() + inner}");
  }

  private record Coord(int i, int j, String color) { }
}
