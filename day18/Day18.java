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
    solve("day18/sample.txt");
//    solve("day18/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    List<Coord> rimPoints = new ArrayList<>();
    rimPoints.add(new Coord(0, 0));
    for (int i = 0; i < lines.size(); i++) {
      String[] split = lines.get(i).split("\\s+");
      String color = split[2].replaceAll("[(#)]", "");
      int depth = Integer.parseInt(color.substring(0, color.length() - 1), 16);
      String direction = color.substring(color.length() - 1);
      var last = rimPoints.getLast();
      switch (direction) {
        case "0" -> {
          var end = last.j() + depth;
          rimPoints.add(new Coord(last.i(), end));
        }
        case "2" -> {
          var end = last.j() - depth;
          rimPoints.add(new Coord(last.i(), end));
        }
        case "1" -> {
          var end = last.i() + depth;
          rimPoints.add(new Coord(end, last.j()));
        }
        case "3" -> {
          var end = last.i() - depth;
          rimPoints.add(new Coord(end, last.j()));
        }
      }
    }

//    if (!rimPoints.getFirst().equals(rimPoints.getLast())) {
//      throw new IllegalStateException(STR. "\{ rimPoints.getFirst() } != \{ rimPoints.getLast() }" );
//    }
//    rimPoints.removeLast();
    System.out.println(rimPoints);
//    part1(rimPoints);
    long sum = 0;
    long rim = 0;
    for (int i = 0; i < rimPoints.size() - 1; i++) {
      Coord a = rimPoints.get(i);
      Coord b = rimPoints.get(i + 1);
      sum += ((long) a.j * b.i - (long) a.i * b.j);
      rim += a.i == b.i ? Math.abs(a.j - b.j) - 1: Math.abs(a.i - b.i) - 1;
    }

    System.out.println(STR. "Sum: \{ sum }; Rim: \{rim}; Area: \{ sum / 2 } Total: \{rim + sum / 2}" );
    // 952408144115
  }

  private static void part1(List<Coord> list) {
    var poly = new Polygon();
    Set<String> rim = new HashSet<>();
    for (Coord c : list) {
      rim.add(STR. "\{ c.i }_\{ c.j }" );
      poly.addPoint(c.j, c.i);
    }
    Rectangle bounds = poly.getBounds();
    int minI = (int) bounds.getMinY();
    int maxI = (int) bounds.getMaxY();
    int minJ = (int) bounds.getMinX();
    int maxJ = (int) bounds.getMaxX();
    System.out.println(STR. "i: \{ minI } -> \{ maxI }; j: \{ minJ } -> \{ maxJ }" );
    int inner = 0;
    for (int i = minI; i <= maxI; i++) {
      for (int j = minJ; j <= maxJ; j++) {
        if (rim.contains(STR. "\{ i }_\{ j }" )) {
          continue;
        }
        if (poly.contains(j, i)) {
//          System.out.println(STR."\{i},\{j} is inside");
          inner += 1;
        }
      }
    }
    System.out.println(STR. "Rim count: \{ rim.size() }" );
    System.out.println(STR. "Inner count: \{ inner }" );
    System.out.println(STR. "Sum from poly: \{ rim.size() + inner }" );
  }

  private record Coord(int i, int j) {
  }
}
