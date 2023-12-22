public class Utils {

  public static long lcm(long a, long b) {
    return a * b / gcd(a, b);
  }

  public static long gcd(long a, long b) {
    if (a == 0) return b;
    while (b != 0) {
      long r;
      if (a > b) {
        r = a % b;
      } else {
        r = b % a;
      }
      a = b;
      b = r;
    }
    return a;
  }
}
