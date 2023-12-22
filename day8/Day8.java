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
//    solve("day8/part2.txt");
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
      String left = split[1].substring(1, 4);
      String right = split[1].substring(6, 9);
      network.put(nodeName, new Node(nodeName, left, right));
    });

    Map<Node, Integer> initSteps = new HashMap<>();
    for (Node start : network.entrySet().stream().filter(n -> n.getKey().endsWith("A"))
        .map(Map.Entry::getValue)
        .toList()) {
      var curr = start;
      int step = 0;
      while (!curr.name().endsWith("Z")) {
        String nextStep = nav[step % nav.length];
        curr = network.get(nextStep.equals("L") ? curr.left : curr.right);
        step++;
      }
      initSteps.put(curr, step);
    }
    List<Integer> steps = initSteps.values().stream().toList();
    Long res = steps.stream().map(i -> (long) i).reduce(Utils::lcm).orElseThrow();
    System.out.println(STR."res = \{res}");
  }

  record Node(String name, String left, String right) {
  }

}
