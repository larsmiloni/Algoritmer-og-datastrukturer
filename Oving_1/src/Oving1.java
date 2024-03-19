// Oppgave 1-1

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Oving1 {

  public static void main(String[] args) {
    Oving1 mainInstance = new Oving1();
    mainInstance.getTime(5000000);
    //mainInstance.getTime(25000000);
    //mainInstance.getTime(125000000);
    //mainInstance.getTime(625000000);
  }

  private String maxProfit(ArrayList<Integer> dataSet) {
    int buyDay = 0;
    int sellDay = 0;
    int maxProfit = 0;
    int currentProfit = 0;
    int potentialBuyDay = 0;

    for (int i = 0; i < dataSet.size(); i++) {
      currentProfit += dataSet.get(i);
      if (currentProfit <= 0) {
        currentProfit = 0;
        potentialBuyDay = i;
      }
      if (currentProfit > maxProfit) {
        maxProfit = currentProfit;
        buyDay = potentialBuyDay + 1;
        sellDay = i + 1;
      }
    }
    return "Buy on day: " + buyDay + "\nSell on day: " + sellDay + "\nMax profit: " + maxProfit;
  }

  private ArrayList<Integer> randomNumbers(int lenght) {
    ArrayList<Integer> numbers = new ArrayList();
    for (int i = 0; i < lenght; i++) {
      Random rand = new Random();
      numbers.add(rand.nextInt(21) - 10);
    }
    return numbers;
  }

  private void getTime(int lengthOfArray) {
    Date start = new Date();
    int runder = 0;
    double tid;
    Date slutt;
    do {
      System.out.println(maxProfit(randomNumbers(lengthOfArray)));
      slutt = new Date();
      ++runder;
    } while (slutt.getTime()-start.getTime() < 100);
    tid = (double)
      (slutt.getTime()-start.getTime()) / runder;
    System.out.println(lengthOfArray + " tall: " + "Millisekund pr. runde: " + tid);
  }
}