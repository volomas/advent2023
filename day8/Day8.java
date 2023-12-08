import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {
  public static void main(String[] args) throws IOException {
//    solve("day8/sample.txt");
//    solve("day8/sample2.txt");
    solve("day8/input.txt");
//    solve("day8/yura.txt");
  }

  static Map<String, Node> network = new HashMap<>();

  private static void solve(String filename) throws IOException {
    Path path = Path.of(filename);
    List<String> lines = Files.readAllLines(path);
    String[] nav = lines.get(0).split("");
    lines.stream().skip(2).forEach(l -> {
      String[] split = l.split("\s+=\s+");
      String nodeName = split[0].trim();
      String left = split[1].substring(1,4);
      String right = split[1].substring(6,9);
      network.put(nodeName, new Node(left, right));
    });

    String curr = "AAA";
    int step = 0;
    while (!curr.equals("ZZZ")) {
      Node next = network.get(curr);
      System.out.println(STR."Step = \{step}; curr = \{curr}");
      String nextStep = nav[step % nav.length];
      if (nextStep.equals("L")) {
        curr = next.left;
      } else {
        curr = next.right;
      }
      step++;
    }

    System.out.println(STR."\{filename} : \{step}");
  }

  record Node(String left, String right){}
}
