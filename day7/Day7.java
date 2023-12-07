import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    put("J", 0);
    put("2", 1);
    put("3", 2);
    put("4", 3);
    put("5", 4);
    put("6", 5);
    put("7", 6);
    put("8", 7);
    put("9", 8);
    put("T", 9);
    put("Q", 11);
    put("K", 12);
    put("A", 13);
  }};

  record Hand(String cards, int bid) implements Comparable<Hand> {

    Strength strength() {
      Map<String, Integer> map = new HashMap<>();
      for (String c : cards.split("")) {
        map.computeIfPresent(c, (k, v) -> v + 1);
        map.putIfAbsent(c, 1);
      }

      Strength originalStrength = switch (map.size()) {
        case 1 -> Strength.FIVE_OF_KIND;
        case 5 -> Strength.HIGH_CARD;
        case 4 -> Strength.ONE_PAIR;
        case 3 -> {
          if (map.values().stream().anyMatch(v -> v == 3)) {
            yield Strength.THREE_OF_KIND;
          } else {
            yield Strength.TWO_PAIR;
          }
        }
        case 2 -> {
          if (map.values().stream().anyMatch(v -> v == 4)) {
            yield Strength.FOUR_OF_KIND;
          } else {
            yield Strength.FULL_HOUSE;
          }
        }
        default -> throw new IllegalStateException();
      };

      int jokers = map.getOrDefault("J", 0);
      if (jokers == 1) {
        return switch (originalStrength) {
          case HIGH_CARD -> Strength.ONE_PAIR;
          case ONE_PAIR -> Strength.THREE_OF_KIND;
          case TWO_PAIR -> Strength.FULL_HOUSE;
          case FOUR_OF_KIND -> Strength.FIVE_OF_KIND;
          case THREE_OF_KIND -> Strength.FOUR_OF_KIND;
          default -> throw new IllegalStateException();
        };
      } else if (jokers == 2) {
        return switch (originalStrength) {
          case FULL_HOUSE -> Strength.FIVE_OF_KIND;
          case ONE_PAIR -> Strength.THREE_OF_KIND;
          case TWO_PAIR -> Strength.FOUR_OF_KIND;
          default -> throw new IllegalStateException();
        };
      } else if (jokers == 3) {
        return switch (originalStrength) {
          case FULL_HOUSE -> Strength.FIVE_OF_KIND;
          case THREE_OF_KIND -> Strength.FOUR_OF_KIND;
          default -> throw new IllegalStateException();
        };
      } else if (jokers == 4) {
        return Strength.FIVE_OF_KIND;
      }

      return originalStrength;
    }

    @Override
    public int compareTo(Hand o) {
      if (this.strength() == o.strength()) {
        var thisCards = cards.split("");
        var otherCards = o.cards().split("");
        for (int i = 0; i < 5; i++) {
          if (!thisCards[i].equals(otherCards[i])) {
            return -cardsStrength.get(thisCards[i]).compareTo(cardsStrength.get(otherCards[i]));
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
