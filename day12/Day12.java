import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12 {
  static Map<String, Long> memo = new HashMap<>();

  public static void main(String[] args) throws IOException {
//    solve("day12/sample.txt");
    solve("day12/input.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    long sum = 0;
    for (String line : lines) {
      String[] parts = line.split("\s+");
      String pattern = parts[0];
      List<Integer> numbers = Arrays.stream(parts[1].split(",")).map(Integer::parseInt).toList();

      pattern = unfoldPattern(pattern);
      numbers = unfoldNums(numbers);
      sum += countWays(pattern, numbers);
    }

    System.out.println(STR. "Total arrengements: \{ sum }" );
  }

  private static List<Integer> unfoldNums(List<Integer> numbers) {
    List<Integer> res = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      res.addAll(numbers);
    }
    return res;
  }

  private static String unfoldPattern(String pattern) {
    List<String> patterns = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      patterns.add(pattern);
    }
    return String.join("?", patterns);
  }

  private static long countWays(String pattern, List<Integer> nums) {
    if (pattern.isEmpty()) {
      return nums.isEmpty() ? 1 : 0;
    }

    if (nums.isEmpty()) {
      return pattern.contains("#") ? 0 : 1;
    }

    String key = pattern + "_" + nums;
    if (memo.containsKey(key)) {
      return memo.get(key);
    }

    long result = 0;
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

    memo.put(key, result);
    return result;
  }
}
