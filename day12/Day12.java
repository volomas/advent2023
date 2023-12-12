import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day12 {
  public static void main(String[] args) throws IOException {
//    solve("day12/sample.txt");
    solve("day12/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    int sum = 0;
    for (String line : lines) {
      String[] parts = line.split("\s+");
      String record = parts[0];
      List<Integer> numbers = Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList();
      sum += countWays(record, numbers);
    }

    System.out.println(STR. "Total arrengements: \{ sum }" );
  }

  private static int countWays(String pattern, List<Integer> nums) {
    if (pattern.isEmpty()) {
      return nums.isEmpty() ? 1 : 0;
    }

    if (nums.isEmpty()) {
      return pattern.contains("#") ? 0 : 1;
    }

    int result = 0;
    char spring = pattern.charAt(0);
    if (spring == '.' || spring == '?') {
      result += countWays(pattern.substring(1), nums);
    }

    if (spring == '#' || spring == '?') {
      Integer firstNum = nums.get(0);
      if (firstNum <= pattern.length()
          && !pattern.substring(0, firstNum).contains(".")
          && (firstNum == pattern.length() || pattern.charAt(firstNum) != '#')) {
        result += countWays(firstNum >= pattern.length() ? "" : pattern.substring(firstNum + 1),
            nums.stream().skip(1).toList());
      }
    }
    return result;
  }
}
