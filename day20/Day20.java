import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Day20 {
  private static boolean LOW = true;
  private static boolean HIGH = false;

  public static void main(String[] args) throws IOException {
//    solve("day20/sample.txt");
//    solve("day20/second.txt");
    solve("day20/input.txt");
  }

  private static void solve(String f) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(f));
    Map<Module, List<String>> targets = new HashMap<>();
    Map<String, Module> modules = new HashMap<>();
    Map<String, Set<String>> conjSet = new HashMap<>();
    for (String line : lines) {
      if (line.startsWith("&")) {
        conjSet.put(line.substring(1, line.indexOf(" ")), new HashSet<>());
      }
    }

    for (String line : lines) {
      if (!line.startsWith("&")) {
        String[] split = line.split("\\s+->\\s+");
        String name = split[0].startsWith("%") ? split[0].substring(1) : split[0];
        Arrays.stream(split[1].split(", ")).filter(conjSet::containsKey).forEach(
            s -> conjSet.get(s).add(name)
        );
      }
    }

    for (String line : lines) {
      String[] split = line.split("\\s+->\\s+");
      String modName = split[0];
      List<String> targetList = Arrays.stream(split[1].split(", ")).toList();
      if (modName.equals("broadcaster")) {
        Module b = new Module() {
          @Override
          public String name() {
            return "broadcaster";
          }

          @Override
          public List<Pulse> sendPulses(Pulse input, List<String> targets) {
            return pulsesTo(input.signal, targets);
          }
        };
        modules.put("broadcaster", b);
        targets.put(b, targetList);
      } else {
        String name = modName.substring(1);
        if (modName.startsWith("%")) {
          FlipFlop key = new FlipFlop(name);
          targets.put(key, targetList);
          modules.put(name, key);
        } else if (modName.startsWith("&")) {
          ConMod key = new ConMod(name, List.copyOf(conjSet.get(name)));
          targets.put(key, targetList);
          modules.put(name, key);
        } else {
          throw new IllegalStateException();
        }
      }
    }

    int low = 0;
    int high = 0;
    for (int i = 1; i <= 1000; i++) {
      System.out.println(STR."Push button \{i}");
      Queue<Pulse> q = new ArrayDeque<>();
      q.add(new Pulse(LOW, "button", "broadcaster"));
      while (!q.isEmpty()) {
        Pulse inPulse = q.poll();
        System.out.println(inPulse);
        if (inPulse.signal == HIGH) {
          high++;
        } else {
          low++;
        }
        Module module = modules.get(inPulse.target);
        List<String> t = targets.get(module);
        if (t != null) {
          for (Pulse pulse : module.sendPulses(inPulse, t)) {
            q.add(pulse);
          }
        }
      }
    }

    System.out.println(STR."Signals: low=\{low}; high=\{high}. Answer = \{low * high}");
  }

  static class FlipFlop implements Module {
    private boolean on = false;
    private final String name;

    public FlipFlop(String name) {
      this.name = name;
    }

    @Override
    public String name() {
      return this.name;
    }

    @Override
    public List<Pulse> sendPulses(Pulse input, List<String> targets) {
      if (input.signal == HIGH) {
        return Collections.emptyList();
      }

      List<Pulse> pulses;
      if (on) {
        pulses = pulsesTo(LOW, targets);
      } else {
        pulses = pulsesTo(HIGH, targets);
      }
      on = !on;
      return pulses;
    }
  }

  static class ConMod implements Module {
    private final String name;
    private final Map<String, Boolean> state = new HashMap<>();

    public ConMod(String name, List<String> inputs) {
      this.name = name;
      inputs.forEach(i -> state.put(i, LOW));
    }

    @Override
    public String name() {
      return this.name;
    }

    @Override
    public List<Pulse> sendPulses(Pulse input, List<String> targets) {
      state.put(input.from, input.signal);
      if (state.values().stream().allMatch(i -> i.equals(HIGH))) {
        return pulsesTo(LOW, targets);
      } else {
        return pulsesTo(HIGH, targets);
      }
    }
  }

  interface Module {
    String name();

    List<Pulse> sendPulses(Pulse input, List<String> targets);

    default List<Pulse> pulsesTo(boolean signal, List<String> targets) {
      return targets.stream().map(t -> new Pulse(signal, this.name(), t)).toList();
    }
  }

  record Pulse(boolean signal, String from , String target) {

    @Override
    public String toString() {
      return STR."\{from} -\{signal == HIGH ? "high" : "low"}-> \{target}";
    }
  }
}
