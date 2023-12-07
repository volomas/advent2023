import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {
  public static void main(String[] args) throws IOException {
    solve("day7/input.txt");
//    solve("day7/sample.txt");
  }

  private static void solve(String filename) throws IOException {
    Path path = Path.of(filename);
    List<String> lines = Files.readAllLines(path);
    List<Hand> hands = lines.stream().map(l -> {
      String[] s = l.split(" ");
      return new Hand(s[0], Integer.parseInt(s[1]));
    }).toList();

    List<Hand> sortedHands = hands.stream().sorted().toList().reversed();
    var res = 0;
    for (int rank = 1; rank <= sortedHands.size(); rank++) {
      res += rank * sortedHands.get(rank - 1).bid();
    }
    System.out.println(STR. "res = \{ res }" );
  }

  enum Strength {
    FIVE_OF_KIND, FOUR_OF_KIND, FULL_HOUSE, THREE_OF_KIND, TWO_PAIR, ONE_PAIR, HIGH_CARD
  }

  static Map<String, Integer> cardsStrength = new HashMap<>() {{
    put("2", 1);
    put("3", 2);
    put("4", 3);
    put("5", 4);
    put("6", 5);
    put("7", 6);
    put("8", 7);
    put("9", 8);
    put("T", 9);
    put("J", 10);
    put("Q", 11);
    put("K", 12);
    put("A", 13);
  }};

  record Hand(String cards, int bid) implements Comparable<Hand> {

    Strength strength() {
      Map<String, Integer> set = new HashMap<>();
      for (String c : cards.split("")) {
        set.computeIfPresent(c, (k, v) -> v + 1);
        set.putIfAbsent(c, 1);
      }

      if (set.size() == 1) return Strength.FIVE_OF_KIND;
      if (set.size() == 5) return Strength.HIGH_CARD;
      if (set.size() == 4) return Strength.ONE_PAIR;
      if (set.size() == 3) {
        if (set.values().stream().anyMatch(v -> v == 3)) {
          return Strength.THREE_OF_KIND;
        } else {
          return Strength.TWO_PAIR;
        }
      }
      if (set.size() == 2) {
        if (set.values().stream().anyMatch(v -> v == 4)) {
          return Strength.FOUR_OF_KIND;
        } else {
          return Strength.FULL_HOUSE;
        }
      }
      throw new IllegalStateException();
    }

    @Override
    public int compareTo(Hand o) {
      if (this.strength() == o.strength()) {
        var thisCards = cards.split("");
        var otherCards = o.cards().split("");
        for (int i = 0; i < 5; i++) {
          if (!thisCards[i].equals(otherCards[i])) {
            return - cardsStrength.get(thisCards[i]).compareTo(cardsStrength.get(otherCards[i]));
          }
        }
      }
      return this.strength().compareTo(o.strength());
    }

    @Override
    public String toString() {
      return STR. "\{ cards } - \{ bid }" ;
    }
  }
}
