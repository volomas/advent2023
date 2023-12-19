import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Day19 {
  public static final String EMPTY = "";
  static Map<String, List<Function<Part, String>>> wMap = new HashMap<>();

  public static void main(String[] args) throws IOException {
//    solve("day19/sample.txt");
    solve("day19/input.txt");
  }

  private static void solve(String file) throws IOException {
    String input = Files.readString(Path.of(file));
    String[] split = input.split("\n\n");
    String[] workflows = split[0].split("\n");
    String[] ratings = split[1].split("\n");
    for (String w : workflows) {
      populateWorkflows(w);
    }
    int total = 0;
    for (String r : ratings) {
      Part p = parsePart(r);
      if (isAccepted(p, "in")) {
        total += p.sum();
      }
    }
    System.out.println(STR."Answer: \{total}");
  }

  private static boolean isAccepted(Part p, String workflowName) {
    for (var instr : wMap.get(workflowName)) {
      String destination = instr.apply(p);
      switch (destination) {
        case "A" -> {
          return true;
        }
        case "R" -> {
          return false;
        }
        case String s when !s.equals(EMPTY) -> {
          return isAccepted(p, s);
        }
        default -> {
        }
      }
    }
    throw new IllegalStateException();
  }

  private static Part parsePart(String r) {
    String[] split = r.substring(1, r.length() - 1).split(",");
    int x = Integer.parseInt(split[0].substring(2));
    int m = Integer.parseInt(split[1].substring(2));
    int a = Integer.parseInt(split[2].substring(2));
    int s = Integer.parseInt(split[3].substring(2));
    return new Part(x, m, a, s);
  }

  private static void populateWorkflows(String w) {
    String wfName = w.substring(0, w.indexOf("{"));
    String[] instructions = w.substring(w.indexOf("{") + 1, w.indexOf("}")).split(",");
    String defaultDestination = instructions[instructions.length - 1];
    instructions = Arrays.copyOf(instructions, instructions.length - 1);
    List<Function<Part, String>> instrList = new ArrayList<>();
    for (String instr : instructions) {
      String letter = String.valueOf(instr.charAt(0));
      String sign = String.valueOf(instr.charAt(1));
      int bound = Integer.parseInt(instr.substring(instr.indexOf(sign) + 1, instr.indexOf(":")));
      String destination = instr.substring(instr.indexOf(":") + 1);
      instrList.add(part -> {
        int partConfig = switch (letter) {
          case "a" -> part.a();
          case "m" -> part.m();
          case "x" -> part.x();
          case "s" -> part.s();
          default -> throw new IllegalStateException("Unexpected value: " + letter);
        };
        if (sign.equals("<")) {
          return partConfig < bound ? destination : EMPTY;
        } else {
          return partConfig > bound ? destination : EMPTY;
        }
      });
    }
    instrList.add(part -> defaultDestination);
    wMap.put(wfName, instrList);
  }

  private record Part(int x, int m, int a, int s) {
    public int sum() {
      return x + m + a + s;
    }
  }
}
