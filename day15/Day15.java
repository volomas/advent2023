import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Day15 {
  public static final int BUCKETS = 256;
  static Map<Label, Integer> hashMap = new HashMap<>(BUCKETS);

  public static void main(String[] args) throws IOException {
//    solve("day15/sample.txt");
    solve("day15/input.txt");
  }

  private record Label(String label) {
    @Override
    public int hashCode() {
      int h = 0;
      for (char c : label.toCharArray()) {
        h += c;
        h *= 17;
        h %= BUCKETS;
      }
      return h;
    }

    public int bucket() {
      return hashCode() % BUCKETS;
    }
  }

  private static void solve(String filename) throws IOException {
    String input = Files.readString(Path.of(filename)).replaceAll("\n", "");
    for (String seq : input.split(",")) {
      if (seq.contains("-")) {
        String label = seq.replace("-", "");
        hashMap.remove(new Label(label));
      } else {
        String label = seq.split("=")[0];
        int focalLen = Integer.parseInt(seq.split("=")[1]);
        hashMap.put(new Label(label), focalLen);
      }
    }

    int sum = 0;
    var slot = 1;
    var prevBucket = -1;
    for (var entry : hashMap.entrySet()) {
      int bucket = entry.getKey().bucket();
      if (prevBucket == bucket) {
        slot++;
      } else {
        prevBucket = bucket;
        slot = 1;
      }
      sum += (bucket + 1) * entry.getValue() * slot;
    }
    System.out.println(STR. "Result = \{ sum }" );
  }
}
