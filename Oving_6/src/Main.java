import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    Graf graf1 = leseGrafFraFil("src/ø6g1");
    Graf graf2 = leseGrafFraFil("src/ø6g2");
    Graf graf3 = leseGrafFraFil("src/ø6g3");
    Graf graf4 = leseGrafFraFil("src/ø6g5");
    Graf graf5 = leseGrafFraFil("src/ø6g7");

    graf4.printTopologiskSortert();
    System.out.println("\n");
    graf5.printTopologiskSortert();
    System.out.println("\n");

    graf1.bfs(graf1.node[0]);
    graf2.bfs(graf2.node[0]);
    graf3.bfs(graf3.node[0]);
    graf4.bfs(graf4.node[0]);
    graf5.bfs(graf5.node[0]);

    graf1.printGrafBFS();
    System.out.println("\n");
    graf2.printGrafBFS();
    System.out.println("\n");
    graf3.printGrafBFS();
    System.out.println("\n");
    graf4.printGrafBFS();
    System.out.println("\n");
    graf5.printGrafBFS();
  }

  public static Graf leseGrafFraFil(String filSti) {
    BufferedReader br = null;
    Graf graf = new Graf();
    try {
      br = new BufferedReader(new FileReader(filSti));
      graf.innlesingAvNabolisteFraFile(br);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return graf;
  }
}