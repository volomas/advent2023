import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Map.entry;

public class Day5 {
  static NavigableMap<Long, Data> seedToSoil = new TreeMap<>();
  static NavigableMap<Long, Data> soilToFert = new TreeMap<>();
  static NavigableMap<Long, Data> fertToWater = new TreeMap<>();
  static NavigableMap<Long, Data> waterToLight = new TreeMap<>();
  static NavigableMap<Long, Data> lightToTemperature = new TreeMap<>();
  static NavigableMap<Long, Data> tempToHumidity = new TreeMap<>();
  static NavigableMap<Long, Data> humidityToLocation = new TreeMap<>();

  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    String input = Files.readString(path);
    String[] sections = input.split("\n\n");
    String[] seedsParsed = sections[0].split(":\s+")[1].split("\s+");
    Map<Long, Long> seedsRange = getAllSeeds(seedsParsed);
    populateMap(seedToSoil, sections[1]);
    populateMap(soilToFert, sections[2]);
    populateMap(fertToWater, sections[3]);
    populateMap(waterToLight, sections[4]);
    populateMap(lightToTemperature, sections[5]);
    populateMap(tempToHumidity, sections[6]);
    populateMap(humidityToLocation, sections[7]);

    Long minLoc = seedsRange.entrySet().stream()
        .flatMap(s -> getTarget(seedToSoil, s).stream())
        .flatMap(s -> getTarget(soilToFert, s).stream())
        .flatMap(s -> getTarget(fertToWater, s).stream())
        .flatMap(s -> getTarget(waterToLight, s).stream())
        .flatMap(s -> getTarget(lightToTemperature, s).stream())
        .flatMap(s -> getTarget(tempToHumidity, s).stream())
        .flatMap(s -> getTarget(humidityToLocation, s).stream())
        .min(Entry.comparingByKey()).orElseThrow().getKey();
    System.out.println(STR. "Min loc = \{ minLoc }" );
  }

  private static Map<Long, Long> getAllSeeds(String[] seedsParsed) {
    Map<Long, Long> seeds = new HashMap<>();
    for (int i = 0; i < seedsParsed.length - 1; i += 2) {
      long seedStart = Long.parseLong(seedsParsed[i]);
      long seedEnd = seedStart - 1 + Long.parseLong(seedsParsed[i + 1]);
      seeds.put(seedStart, seedEnd);
    }
    return seeds;
  }

  private static List<Entry<Long, Long>> getTarget(NavigableMap<Long, Data> map, Entry<Long, Long> sourceRange) {
    SortedMap<Long, Data> sub = map.subMap(sourceRange.getKey(), true, sourceRange.getValue(), true);

    if (sub.isEmpty()) {
      Entry<Long, Data> floor = map.floorEntry(sourceRange.getKey());
      Entry<Long, Data> ceil = map.ceilingEntry(sourceRange.getValue());
      if (floor != null && ceil != null && floor.getValue().same(ceil.getValue())) {
        return List.of(
            entry(floor.getValue().target(sourceRange.getKey()), floor.getValue().target(sourceRange.getValue()))
        );
      }
      return List.of(sourceRange);
    }

    List<Entry<Long, Long>> ranges = new ArrayList<>();

    Collection<Data> values = sub.values();
    sub.entrySet().stream()
        .filter(e -> e.getValue().end())
        .filter(e -> e.getValue().singlePoint() || values.contains(e.getValue().asStart()))
        .forEach(e -> {
          ranges.add(entry(e.getValue().targetStart(), e.getValue().targetEnd()));
        });

    sub.entrySet().stream()
        .filter(e -> e.getValue().end())
        .filter(e -> !e.getValue().singlePoint())
        .filter(e -> !values.contains(e.getValue().asStart()))
        .forEach(e -> {
          ranges.add(entry(e.getValue().target(sourceRange.getKey()), e.getValue().targetEnd()));
        });

    sub.entrySet().stream()
        .filter(e -> !e.getValue().end())
        .filter(e -> !values.contains(e.getValue().asEnd()))
        .forEach(e -> {
          ranges.add(entry(e.getValue().targetStart(), e.getValue().target(sourceRange.getValue())));
        });

    Entry<Long, Data> prev = null;
    for (var entry : sub.entrySet()) {
      if (prev == null && !entry.getValue().end() && entry.getKey() > sourceRange.getKey()) {
        ranges.add(entry(sourceRange.getKey(), entry.getKey() - 1));
      }
      if (prev != null && !prev.getValue().same(entry.getValue()) && (entry.getKey() - prev.getKey()) > 1) {
        ranges.add(entry(prev.getKey() + 1, entry.getKey() - 1));
      }
      prev = entry;
    }

    if (prev != null && prev.getValue().end() && prev.getKey() < sourceRange.getValue()) {
      ranges.add(entry(prev.getKey() + 1, sourceRange.getValue()));
    }
    return ranges;
  }

  private static void populateMap(NavigableMap<Long, Data> map, String section) {
    String[] rows = section.split("\n");
    for (int i = 1; i < rows.length; i++) {
      List<Long> rowNums = Arrays.stream(rows[i].split(" ")).map(Long::parseLong).toList();
      Data value = new Data(rowNums.get(1), rowNums.get(0), rowNums.get(2), false);
      map.put(rowNums.get(1), value);
      map.put(value.sourceEnd(), value.asEnd());
    }
  }

  private record Data(long sourceStart, long targetStart, long len, boolean end) {

    public boolean singlePoint() {
      return len == 1;
    }

    public long sourceEnd() {
      return sourceStart + len - 1;
    }

    public long targetEnd() {
      return targetStart + len - 1;
    }

    public long offset() {
      return targetStart - sourceStart;
    }

    public long target(long num) {
      if (num >= sourceStart && num <= sourceEnd()) {
        return num + offset();
      } else {
        return num;
      }
    }

    Data asEnd() {
      return new Data(sourceStart, targetStart, len, true);
    }

    Data asStart() {
      return new Data(sourceStart, targetStart, len, false);
    }

    boolean same(Data other) {
      return this.sourceStart == other.sourceStart;
    }

    @Override
    public String toString() {
      return STR. "(\{ sourceStart() }..\{ sourceEnd() } -> \{ target(sourceStart()) }..\{ target(sourceEnd()) })" + (singlePoint() ? "P" : "");

    }
  }
}
