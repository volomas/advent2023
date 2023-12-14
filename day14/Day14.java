import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {
  public static final int CYCLES = 1_000_000_000;

  public static void main(String[] args) throws IOException {
//    solve("day14/volo.txt");
//    solve("day14/sample.txt");
    solve("day14/input.txt");
//    solve("day14/yura.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));

    Map<Integer, Integer> scores = new HashMap<>();
    List<Integer> scoresSoFar = new ArrayList<>();
    int loopLen = 0;
    int beginsAt = 0;
    for (int i = 1; i <= CYCLES; i++) {
      lines = reposition(transpose(lines), true);
      lines = reposition(transpose(lines), true);
      lines = reposition(transpose(lines), false);
      lines = reposition(transpose(lines), false);

      int score = score(lines);
      System.out.println(STR. "\{ i }: \{ score }" );
      if (scores.containsKey(score)) {
        int diff = i - scores.get(score);
        if (diff == loopLen) {
          beginsAt = scores.get(score);
          System.out.println(STR. "Found a loop on cycle \{ i }: each \{ loopLen }. Begins at \{beginsAt}" );
          break;
        }
        loopLen = diff;
      }
      scores.put(score, i);
      scoresSoFar.add(score);
    }

    var repeatingScores = scoresSoFar.subList(scoresSoFar.size() - loopLen, scoresSoFar.size());
    // Answer is among these scores
    System.out.println(STR. "Repeating scores \{ repeatingScores } (\{repeatingScores.size()})" );

    // TODO Probably there is a bug in loop detection or here, because doesn't work on sample
    int answer = repeatingScores.get((CYCLES - beginsAt) % loopLen);
    System.out.println(STR. "Answer: \{ answer }" );
  }

  private static int score(List<String> lines) {
    int res = 0;
    for (String r : transpose(lines)) {
      for (int i = 1; i <= r.length(); i++) {
        if (r.charAt(i - 1) == 'O') res += r.length() - i + 1;
      }
    }
    return res;
  }

  private static List<String> reposition(List<String> tLines, boolean left) {
    List<String> repositioned = new ArrayList<>();
    for (String line : tLines) {
      String[] split = line.split("");

      for (int i = 0; i < split.length; i++) {
        for (int j = 0; j < split.length - i - 1; j++) {
          if ((left && split[j].equals(".") && split[j + 1].equals("O"))
              ||
              (!left && split[j].equals("O") && split[j + 1].equals("."))
          ) {
            var temp = split[j];
            split[j] = split[j + 1];
            split[j + 1] = temp;
          }
        }
      }
      repositioned.add(String.join("", split));
    }
    return repositioned;
  }

  private static List<String> transpose(List<String> lines) {
    var res = new ArrayList<String>();
    for (int i = 0; i < lines.get(0).length(); i++) {
      String transposedRow = "";
      for (String line : lines) {
        transposedRow += line.charAt(i);
      }
      res.add(transposedRow);
    }

    return res;
  }
}
