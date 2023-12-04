import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day4 {

  public static void main(String[] args) throws IOException {
    Path path = Path.of(args[0]);
    int cardIdx = 0;
    List<String> lines = Files.readAllLines(path);
    int[] cardsScore = new int[lines.size()];
    for (String line : lines) {
      String[] card = line.split(":\s+")[1].trim().split(" \\| ");
      Set<String> hand = Arrays.stream(card[0].split(" "))
          .map(String::trim)
          .filter(s -> !s.isBlank())
          .collect(Collectors.toSet());

      Set<String> winning = Arrays.stream(card[1].split(" "))
          .map(String::trim)
          .filter(s -> !s.isBlank())
          .collect(Collectors.toSet());

      hand.retainAll(winning);
      cardsScore[cardIdx++] = hand.size();
    }

    int[] cards = new int[cardsScore.length];
    Arrays.fill(cards, 1);

    for (int i = 0; i < cards.length - 1; i++) {
      for (int j = 1; j <= cardsScore[i]; j++) {
        int nextIdx = i + j;
        for (var k = 0; k < cards[i]; k++) {
          cards[nextIdx] += 1;
        }
      }
    }

    int res = Arrays.stream(cards).sum();
    System.out.println(STR. "part 2: \{ res }" );
  }
}
