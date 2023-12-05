import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

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
    List<Long> seeds = Arrays.stream(seedsParsed).map(Long::parseLong).toList();
    populateMap(seedToSoil, sections[1]);
    populateMap(soilToFert, sections[2]);
    populateMap(fertToWater, sections[3]);
    populateMap(waterToLight, sections[4]);
    populateMap(lightToTemperature, sections[5]);
    populateMap(tempToHumidity, sections[6]);
    populateMap(humidityToLocation, sections[7]);

    List<Long> locations = new ArrayList<>();
    for (Long s : seeds) {
      long soil = getTarget(seedToSoil, s);
      long fert = getTarget(soilToFert, soil);
      long water = getTarget(fertToWater, fert);
      long light = getTarget(waterToLight, water);
      long temp = getTarget(lightToTemperature, light);
      long hum = getTarget(tempToHumidity, temp);
      long loc = getTarget(humidityToLocation, hum);
      locations.add(loc);
    }

    Long min = locations.stream().min(Comparator.naturalOrder()).orElseThrow();
    System.out.println(STR."Min loc = \{min}");
  }

  private static long getTarget(NavigableMap<Long, Data> map, long source) {
    Map.Entry<Long, Data> longDataEntry = map.floorEntry(source);
    if (longDataEntry == null) {
      return source;
    } else {
      return longDataEntry.getValue().target(source);
    }
  }

  private static void populateMap(NavigableMap<Long, Data> map, String section) {
    String[] rows = section.split("\n");
    for (int i = 1; i < rows.length; i++) {
      List<Long> rowNums = Arrays.stream(rows[i].split(" ")).map(Long::parseLong).toList();
      Data value = new Data(rowNums.get(1), rowNums.get(0), rowNums.get(2));
      map.put(rowNums.get(1), value);
    }
  }

  private record Data(long sourceStart, long targetStart, long len) {
    public long sourceEnd() {
      return sourceStart + len - 1;
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
  }
}
