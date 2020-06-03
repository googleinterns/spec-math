import java.util.List;

public class MathUtils {

  public int add(int a, int b) {
    return a + b;
  }

  public int sumOfArray(List<Integer> numbers) {
    int sum = 0;
    for (Integer number : numbers) {
      sum += number;
    }
    return sum;
  }

  public double divide(int a, int b) {
    return a / b;
  }

  public int computeArea(int r) {
    return (int) (Math.PI * Math.pow(r, 2));
  }
}
