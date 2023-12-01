import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day1 {
  static List<String> digits = List.of(
      "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
  );
  static String regex = "(one|two|three|four|five|six|seven|eight|nine|[1-9])";
  static Pattern firstPattern = Pattern.compile(regex);
  static Pattern lastPattern = Pattern.compile(".*" + regex);

  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    int result = Files.lines(path)
        .map(Day1::extractFirstAndLast)
        .mapToInt(Integer::parseInt)
        .sum();
    System.out.println("day 1 part 1: " + result);
  }

  private static String extractFirstAndLast(String l) {
    Matcher firstMatcher = firstPattern.matcher(l);
    firstMatcher.find();
    String firstDigit = firstMatcher.group(1);
    Matcher lastMatcher = lastPattern.matcher(l);
    lastMatcher.find();
    String lastDigit = lastMatcher.group(1);

    return toDigit(firstDigit) + toDigit(lastDigit);
  }

  private static String toDigit(String s) {
    int i = digits.indexOf(s);
    if (i < 0) {
      return s;
    } else {
      return String.valueOf(i + 1);
    }
  }
}
