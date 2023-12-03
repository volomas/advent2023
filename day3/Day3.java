import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class Day3 {

  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    int row = 0;
    var nums = new HashMap<Point, Num>();
    var gears = new HashSet<Point>();
    for (String line : Files.readAllLines(path)) {
      line = line.trim();
      var col = 0;
      String num = "";
      String[] cells = line.split("");
      for (String cell : cells) {
        boolean isDigit = cell.matches("\\d");
        if (isDigit) {
          num += cell;
        }

        if (!isDigit || col == cells.length - 1) {
          if (!num.isBlank()) {
            var n = new Num(Integer.parseInt(num), false);
            int end = isDigit ? col : col - 1;
            int start = end - num.length() + 1;
            for (var i = start; i <= end; i++) {
              nums.put(new Point(row, i), n);
            }
            num = "";
          }
        }

        if (cell.equals("*")) {
          gears.add(new Point(row, col));
        }

        col++;
      }
      row++;
    }

    int res = 0;
    for (var gear : gears) {
      Set<Num> numsTouchingGear = Collections.newSetFromMap(new IdentityHashMap<>());
      gear.adjacent().forEach(p -> {
        Num num = nums.get(p);
        if (num != null) {
          num.isPart = true;
          numsTouchingGear.add(num);
        }
      });

      if (numsTouchingGear.size() >= 2) {
        var ratio = numsTouchingGear.stream()
            .mapToInt(n -> n.num)
            .reduce(1, (a, b) -> a * b);
        res += ratio;
      }
    }

    System.out.println(STR. "part 2: \{ res }" );
  }

  private record Point(int i, int j) {
    public List<Point> adjacent() {
      return List.of(
          new Point(i - 1, j),
          new Point(i - 1, j - 1),
          new Point(i - 1, j + 1),

          new Point(i + 1, j),
          new Point(i + 1, j + 1),
          new Point(i + 1, j - 1),

          new Point(i, j - 1),
          new Point(i, j + 1)
      );
    }
  }

  private static class Num {
    int num;
    boolean isPart;

    public Num(int num, boolean isPart) {
      this.num = num;
      this.isPart = isPart;
    }
  }
}
