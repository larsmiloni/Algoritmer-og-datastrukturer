import java.util.Date;

public class Oving2 {

  public static void main(String[] args) {
    Oving2 main = new Oving2();

    // 13 x 2.5 = 32.5
    System.out.println(main.sum1(13, 2.5));
    System.out.println(main.sum2(13, 2.5));

    // 14 x 10.1 = 141.4
    System.out.println(main.sum1(14, 10.1));
    System.out.println(main.sum2(14, 10.1));

    //Tidtakning
    main.getTimeForSum1(150, 10.55);
    main.getTimeForSum2(150, 100.55);
  }

  private double sum1(int n, double x) {
    if (n == 1) return x;
    return x + sum1(n - 1, x);
  }

  private double sum2(int n, double x) {
    if (n == 1) return x;
    if (n % 2 == 0) {
      return sum2(n / 2, x + x);
    }
    return x + sum2((n - 1) / 2, x + x);
  }

  private void getTimeForSum1(int n, double x) {
    Date start = new Date();
    int runder = 0;
    double tid;
    Date slutt;
    do {
      sum1(n, x);
      slutt = new Date();
      ++runder;
    } while (slutt.getTime() - start.getTime() < 1000);
    tid = (double)
      (slutt.getTime() - start.getTime()) / runder;
    System.out.println("Sum1:   Millisekund pr. runde:" + tid);
  }

  private void getTimeForSum2(int n, double x) {
    Date start = new Date();
    int runder = 0;
    double tid;
    Date slutt;
    do {
      sum2(n, x);
      slutt = new Date();
      ++runder;
    } while (slutt.getTime() - start.getTime() < 1000);
    tid = (double)
      (slutt.getTime() - start.getTime()) / runder;
    System.out.println("Sum2:   Millisekund pr. runde:" + tid);
  }
}