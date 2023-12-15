import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Day15 {
  public static final int BUCKETS = 256;
  //todo I wonder if Java's HashMap would work?
  static LinkedList<Label>[] hashMap = new LinkedList[BUCKETS];

  public static void main(String[] args) throws IOException {
//    solve("day15/sample.txt");
    solve("day15/input.txt");
  }

  private record Label(String label, int focalLen) {
    @Override
    public boolean equals(Object o) {
      if (o instanceof Label) {
        return Objects.equals(label, ((Label) o).label());
      }
      throw new IllegalStateException();
    }

    @Override
    public int hashCode() {
      return hash(label);
    }

    public int bucket() {
      return hash(label) % BUCKETS;
    }

    @Override
    public String toString() {
      return STR. "[\{ label } \{ focalLen }]" ;
    }
  }

  private static void solve(String filename) throws IOException {
    String input = Files.readString(Path.of(filename)).replaceAll("\n", "");
    int sum = 0;
    for (String seq : input.split(",")) {
      if (seq.contains("-")) {
        String label = seq.replace("-", "");
        Label l = new Label(label, 0);
        LinkedList<Label> labels = hashMap[l.bucket()];
        if (labels != null) {
          labels.remove(l);
        }
      } else {
        String label = seq.split("=")[0];
        int focalLen = Integer.parseInt(seq.split("=")[1]);
        Label l = new Label(label, focalLen);
        LinkedList<Label> lenses = hashMap[l.bucket()];
        if (lenses == null) {
          hashMap[l.bucket()] = new LinkedList<>(List.of(l));
        } else {
          int slot = lenses.indexOf(l);
          if (slot >= 0) {
            lenses.remove(slot);
            lenses.add(slot, l);
          } else {
            lenses.addLast(l);
          }
        }
      }
    }

    for (int i = 0; i < hashMap.length; i++) {
      if (hashMap[i] != null) {
        System.out.println(STR. "Bucket \{ i }: \{ hashMap[i] }" );
        for (int slot = 1; slot <= hashMap[i].size(); slot++) {
          Label label = hashMap[i].get(slot - 1);
          int power = (i + 1) * slot * label.focalLen;
          sum += power;
        }
      }
    }

    System.out.println(STR. "Result = \{ sum }" );
  }

  private static int hash(String seq) {
    int h = 0;
    for (char c : seq.toCharArray()) {
      h += c;
      h *= 17;
      h %= BUCKETS;
    }
    return h;
  }
}
