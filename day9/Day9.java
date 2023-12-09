import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {
  public static void main(String[] args) throws IOException {
//    solve("day9/sample.txt");
    solve("day9/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<int[]> lines = Files.readAllLines(Path.of(filename)).stream()
        .map(l -> Arrays.stream(l.split("\s+")).mapToInt(Integer::parseInt).toArray())
        .toList();

    int res = lines.stream().map(Day9::extrapolate).reduce(0, Integer::sum);
    System.out.println(STR."Res = \{res}");
  }

  private static int extrapolate(int[] seq) {
    List<Integer> firstDigits = new ArrayList<>();
    firstDigits.add(seq[0]);

    int[] curr = seq;
    while (!allZeros(curr)) {
      int[] diff = new int[curr.length - 1];
      for (int i = 1; i < curr.length; i++) {
        diff[i - 1] = curr[i] - curr[i - 1];
      }
      firstDigits.add(diff[0]);
      curr = diff;
    }

    int acc = firstDigits.get(firstDigits.size() - 1);
    for (int i = firstDigits.size() - 2; i >= 0; i--) {
      acc = firstDigits.get(i) - acc;
    }
    return acc;
  }

  private static boolean allZeros(int[] seq) {
    boolean allZeros = true;
    for (int i = 0; i < seq.length; i++) {
      if (seq[i] != 0) return false;
    }
    return allZeros;
  }
}
