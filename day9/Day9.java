import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day9 {
  public static void main(String[] args) throws IOException {
//    solve("day9/sample.txt");
    solve("day9/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<int[]> lines = Files.readAllLines(Path.of(filename))
        .stream()
        .map(l -> Arrays.stream(l.split("\s+")).mapToInt(Integer::parseInt).toArray())
        .toList();

    int res = 0;
    for (int[] seq : lines) {
      res += extrapolate(seq);
    }
    System.out.println(STR."Res = \{res}");
  }

  private static int extrapolate(int[] seq) {
    List<Integer> lastDigits = new ArrayList<>();
    lastDigits.add(seq[seq.length - 1]);

    int[] curr = seq;
    while (!allZeros(curr)) {
      System.out.println(Arrays.stream(curr).boxed().toList());
      int[] diff = new int[curr.length - 1];
      for (int i = 1; i < curr.length; i++) {
        diff[i - 1] = curr[i] - curr[i - 1];
      }
      lastDigits.add(diff[diff.length - 1]);
      curr = diff;
    }

    System.out.println(STR."Last: \{lastDigits}");
    int acc = lastDigits.get(lastDigits.size() - 1);
    for (int i = lastDigits.size() - 2; i >= 0; i--) {
      acc += lastDigits.get(i);
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
