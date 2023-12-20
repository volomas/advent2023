import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Day17 {
  public static void main(String[] args) throws IOException {
//    solve("day17/sample.txt");
//    solve("day17/s2.txt");
//    solve("day17/s3.txt");
//    solve("day17/s4.txt");
//    solve("day17/s6.txt");
//    solve("day17/s5.txt");
    solve("day17/input.txt");
//    solve("day17/yura.txt");
  }

  private static void solve(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    int[][] grid = new int[lines.size()][lines.get(0).length()];
    for (int i = 0; i < lines.size(); i++) {
      String[] row = lines.get(i).split("");
      for (int j = 0; j < row.length; j++) {
        int cell = Integer.parseInt(row[j]);
        grid[i][j] = cell;
      }
    }

    int cost = findWayOut(grid);
    System.out.println(STR. "Cost: \{ cost }" );
  }

  private static int findWayOut(int[][] grid) {
    Map<Cell, Integer> costSoFar = new HashMap<>();
    Map<Cell, Cell> cameFrom = new HashMap<>();
    PriorityQueue<CellWithVal> q = new PriorityQueue<>(Comparator.comparing(CellWithVal::cost));
    var start = new Cell(0, 0, Direction.LEFT, 0);
    cameFrom.put(start, null);
    costSoFar.put(start, 0);
    q.add(new CellWithVal(start, 0));
    while (!q.isEmpty()) {
      var curr = q.remove();
      if (curr.cell().i() == grid.length - 1
          && curr.cell().j() == grid[0].length - 1
          && curr.cell().oneDirectionSteps >= 3) {
        printPath(grid, cameFrom, curr.cell());
        return costSoFar.get(curr.cell());
      }

      for (Cell next : getNext(grid, cameFrom, curr.cell())) {
        int newCost = costSoFar.get(curr.cell()) + costToEnter(grid, next);
        if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
          costSoFar.put(next, newCost);
          cameFrom.put(next, curr.cell());
          q.add(new CellWithVal(next, newCost));
        }
      }
    }

    return -1;
  }

  private static void printPath(int[][] grid, Map<Cell, Cell> cameFrom, Cell cell) {
    Cell a = cell;
    String[][] strGrid = toStrGrid(grid);
    while (a != null) {
      var prev = cameFrom.get(a);
      if (prev == null) {
        strGrid[a.i][a.j] = ">";
        a = prev;
        continue;
      }

      if (prev.i < a.i) {
        strGrid[a.i][a.j] = "v";
      } else if (prev.i > a.i) {
        strGrid[a.i][a.j] = "^";
      } else if (prev.j < a.j) {
        strGrid[a.i][a.j] = ">";
      } else if (prev.j > a.j) {
        strGrid[a.i][a.j] = "<";
      }

      a = prev;
    }
    print(strGrid);
  }

  private static String[][] toStrGrid(int[][] grid) {
    var res = new String[grid.length][grid[0].length];
    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {
        res[i][j] = "" + grid[i][j];
      }
    }
    return res;
  }

  private static List<Cell> getNext(int[][] grid, Map<Cell, Cell> cameFrom, Cell curr) {
    Cell prev = cameFrom.get(curr);
    if (prev == null) {
      return List.of(
          new Cell(curr.i(), curr.j() + 1, Direction.LEFT, 0),
          new Cell(curr.i() + 1, curr.j(), Direction.UP, 0)
      );
    } else {
      List<Cell> nextCells = new ArrayList<>();
      if (prev.i() == curr.i()) {
        if (curr.oneDirectionSteps >= 3) {
          nextCells.add(new Cell(curr.i() - 1, curr.j(), Direction.DOWN, 0));
          nextCells.add(new Cell(curr.i() + 1, curr.j(), Direction.UP, 0));
        }

        if (curr.oneDirectionSteps <= 8) {
          nextCells.add(prev.j() < curr.j()
              ? new Cell(curr.i(), curr.j() + 1, Direction.LEFT, curr.oneDirectionSteps + 1)
              : new Cell(curr.i(), curr.j() - 1, Direction.RIGHT, curr.oneDirectionSteps + 1));
        }
      } else if (prev.j() == curr.j()) {
        if (curr.oneDirectionSteps >= 3) {
          nextCells.add(new Cell(curr.i(), curr.j() - 1, Direction.RIGHT, 0));
          nextCells.add(new Cell(curr.i(), curr.j() + 1, Direction.LEFT, 0));
        }

        if (curr.oneDirectionSteps <= 8) {
          nextCells.add(prev.i() < curr.i()
              ? new Cell(curr.i() + 1, curr.j(), Direction.DOWN, curr.oneDirectionSteps + 1)
              : new Cell(curr.i() - 1, curr.j(), Direction.UP, curr.oneDirectionSteps + 1));
        }
      } else {
        throw new IllegalStateException();
      }

      return nextCells.stream()
          .filter(c -> c.i() >= 0 && c.i() < grid.length && c.j() >= 0 && c.j() < grid[c.i()].length)
          .toList();
    }
  }

  private static int costToEnter(int[][] grid, Cell cell) {
    return grid[cell.i()][cell.j()];
  }

  private static <T> void print(T[][] grid) {
    Arrays.stream(grid).forEach(r -> {
      Arrays.stream(r).forEach(System.out::print);
      System.out.println();
    });
  }

  record Cell(int i, int j, Direction fromDirection, int oneDirectionSteps) {
    @Override
    public String toString() {
      return STR. "[\{ i }, \{ j }]" ;
    }
  }

  record CellWithVal(Cell cell, int cost) {
  }

  enum Direction {UP, DOWN, LEFT, RIGHT;}
}
