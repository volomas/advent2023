import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day2 {
  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    int res = Files.lines(path)
        .map(Day2::colorProduct)
        .mapToInt(i -> i).sum();

    System.out.println(STR. "part 2: \{ res }" );
  }

  private static int colorProduct(String line) {
    String[] sets = line.split(":")[1].split(";");
    var colors = new HashMap<>(Map.of("red", 1, "green", 1, "blue", 1));
    Arrays.stream(sets)
        .map(s -> s.split(", "))
        .flatMap(Arrays::stream)
        .map(String::trim)
        .map(cube -> cube.split(" "))
        .forEach(cube -> {
          int num = Integer.parseInt(cube[0]);
          String color = cube[1];
          colors.compute(color, (k, v) -> num > v ? num : v);
        });

    return colors.values().stream().reduce(1, (i, j) -> i * j);
  }
}
