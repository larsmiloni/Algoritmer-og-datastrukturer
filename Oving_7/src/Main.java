import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    Graf graf1 = lesGrafFraFil("src/flytgraf1");
    Graf graf2 = lesGrafFraFil("src/flytgraf2");
    Graf graf3 = lesGrafFraFil("src/flytgraf3");
    Graf graf4 = lesGrafFraFil("src/flytgraf4");
    Graf graf5 = lesGrafFraFil("src/flytgraf5");

    System.out.println("Graf 1:");
    graf1.maxFlow(0, 7);
    System.out.println("\nGraf 2:");
    graf2.maxFlow(0, 1);
    System.out.println("\nGraf 3:");
    graf3.maxFlow(0, 1);
    System.out.println("\nGraf 4:");
    graf4.maxFlow(0, 7);
    System.out.println("\nGraf 5:");
    graf5.maxFlow(0, 7);
  }

  public static Graf lesGrafFraFil(String filSti) {
    BufferedReader br = null;
    Graf graf = new Graf();
    try {
      br = new BufferedReader(new FileReader(filSti));
      graf.ny_vgraf(br);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return graf;
  }
}
