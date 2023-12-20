import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class Day19 {
  public static final int MAX = 4000;
  static Map<String, List<Instruction>> wMap = new HashMap<>();
  static List<Result> accepted = new ArrayList<>();

  public static void main(String[] args) throws IOException {
//    solve("day19/sample.txt");
    solve("day19/input.txt");
  }

  private static void solve(String file) throws IOException {
    String input = Files.readString(Path.of(file));
    String[] split = input.split("\n\n");
    String[] workflows = split[0].split("\n");
    for (String w : workflows) {
      populateWorkflows(w);
    }
    System.out.println(wMap);

    var xs = new TreeSet<>(List.of(1, MAX));
    var as = new TreeSet<>(List.of(1, MAX));
    var ms = new TreeSet<>(List.of(1, MAX));
    var ss = new TreeSet<>(List.of(1, MAX));

    findAccepted("in", new Result(xs, ms, as, ss));

    long answer = 0;
    System.out.println("Accepted: ");
    accepted.forEach(System.out::println);
    for (int i = 0; i < accepted.size(); i++) {
      Result res = accepted.get(i);
      TreeSet<Integer> a = res.as();
      TreeSet<Integer> m = res.ms();
      TreeSet<Integer> x = res.xs();
      TreeSet<Integer> s = res.ss();
      answer += (long) ((a.last() - a.first()) + 1)
          * ((m.last() - m.first()) + 1) * ((x.last() - x.first()) + 1) * ((s.last() - s.first()) + 1);
    }

    System.out.println(STR. "Accepted parts: \{ answer }" );
  }

  private static void findAccepted(String workflowName, Result ranges) {
    if (workflowName.equals("R")) {
      return;
    } else if (workflowName.equals("A")) {
      accepted.add(ranges);
      return;
    }
    List<Instruction> instructions = wMap.get(workflowName);
    for (int i = 0; i < instructions.size(); i++) {
      Result copy = ranges.copy();

      for (int j = 0; j < i; j++) {
        Instruction notMatchedInstruction = instructions.get(j);
        TreeSet<Integer> configRange = copy.get(notMatchedInstruction.config());
        if (notMatchedInstruction.lessThan()) {
          Integer floor = configRange.floor(notMatchedInstruction.value);
          configRange.remove(floor);
          configRange.add(notMatchedInstruction.value);
        } else {
          Integer ceiling = configRange.ceiling(notMatchedInstruction.value);
          configRange.remove(ceiling);
          configRange.add(notMatchedInstruction.value);
        }
      }

      Instruction matchedInstruction = instructions.get(i);
      if (!matchedInstruction.isDefault()) {
        TreeSet<Integer> configRange = copy.get(matchedInstruction.config());
        if (matchedInstruction.lessThan) {
          Integer ceil = configRange.ceiling(matchedInstruction.value - 1);
          configRange.remove(ceil);
          configRange.add(matchedInstruction.value - 1);
        } else {
          Integer floor = configRange.floor(matchedInstruction.value + 1);
          configRange.remove(floor);
          configRange.add(matchedInstruction.value + 1);
        }
      }

      findAccepted(matchedInstruction.destination, copy);
    }
  }

  private static void populateWorkflows(String w) {
    String wfName = w.substring(0, w.indexOf("{"));
    String[] instructions = w.substring(w.indexOf("{") + 1, w.indexOf("}")).split(",");
    String defaultDestination = instructions[instructions.length - 1];
    instructions = Arrays.copyOf(instructions, instructions.length - 1);
    List<Instruction> instrList = new ArrayList<>();
    for (String instr : instructions) {
      String letter = String.valueOf(instr.charAt(0));
      String sign = String.valueOf(instr.charAt(1));
      int bound = Integer.parseInt(instr.substring(instr.indexOf(sign) + 1, instr.indexOf(":")));
      String destination = instr.substring(instr.indexOf(":") + 1);
      instrList.add(switch (sign) {
        case "<" -> new Instruction(letter, bound, true, destination);
        case ">" -> new Instruction(letter, bound, false, destination);
        default -> throw new IllegalStateException();
      });
    }
    instrList.add(Instruction.createDefault(defaultDestination));
    wMap.put(wfName, instrList);
  }

  private record Instruction(String config, int value, boolean lessThan, String destination) {

    static Instruction createDefault(String destination) {
      return new Instruction("DEFAULT", 0, false, destination);
    }

    boolean isDefault() {
      return config.equals("DEFAULT");
    }

    @Override
    public String toString() {
      if (isDefault()) {
        return STR. "[DEFAULT:\{ destination }]" ;
      } else {
        return STR. "[\{ config }\{ lessThan ? "<" : ">" }\{ value }:\{ destination }]" ;
      }
    }
  }

  private record Result(TreeSet<Integer> xs, TreeSet<Integer> ms, TreeSet<Integer> as, TreeSet<Integer> ss) {

    public TreeSet<Integer> get(String config) {
      return switch (config) {
        case "x" -> xs;
        case "m" -> ms;
        case "a" -> as;
        case "s" -> ss;
        default -> throw new IllegalStateException();
      };
    }

    public Result copy() {
      return new Result(
          new TreeSet<>(xs),
          new TreeSet<>(ms),
          new TreeSet<>(as),
          new TreeSet<>(ss)
      );
    }
  }
}
