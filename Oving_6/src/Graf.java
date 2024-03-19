import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Graf {
  int N, K;
  Node[] node;

  public void innlesingAvNabolisteFraFile(BufferedReader br) throws IOException {
    StringTokenizer st = new StringTokenizer(br.readLine());
    N = Integer.parseInt(st.nextToken());
    node = new Node[N];
    for (int i = 0; i < N; ++i) node[i] = new Node();
    K = Integer.parseInt(st.nextToken());
    for (int i = 0; i < K; ++i) {
      st = new StringTokenizer(br.readLine());
      int fra = Integer.parseInt(st.nextToken());
      int til = Integer.parseInt(st.nextToken());
      Kant k = new Kant(node[til], node[fra].kant1);
      node[fra].kant1 = k;
    }
  }

  public void initforgj(Node s) {
    for (int i = N; i-->0;) {
      node[i].d = new Forgj();
    }
    ((Forgj)s.d).dist = 0;
  }

  public void bfs(Node s) {
    initforgj(s);
    Kø kø = new Kø(N - 1);
    kø.leggIKø(s);
    while (!kø.tom()) {
      Node n = (Node)kø.nesteIKø();
      for (Kant k = n.kant1; k != null; k = k.neste) {
        Forgj f = (Forgj)k.til.d;
        if (f.dist == f.uendelig) {
          f.dist = ((Forgj)n.d).dist + 1;
          f.forgj = n;
          kø.leggIKø(k.til);
        }
      }
    }
  }

  public Node df_topo(Node n, Node l) {
    Topo_lst nd = (Topo_lst)n.d;
    if (nd.funnet) return l;
    nd.funnet = true;
    for (Kant k = n.kant1; k != null; k = k.neste) {
      l = df_topo(k.til, l);
    }
    nd.neste = l;
    return n;
  }

  public Node topologisort() {
    Node l = null;
    for (int i = N; i-->0;) {
      node[i].d = new Topo_lst();
    }
    for (int i = N; i-->0;) {
      l = df_topo(node[i], l);
    }
    return l;
  }


  public void printGrafBFS() {
    System.out.println("Node, Forgj, Dist");
    for (int i = 0; i < N; ++i) {
      Node n = node[i];
      Forgj f = (Forgj) n.d;
      String forgjString = (f.forgj == null) ? "null" : String.valueOf(Arrays.asList(node).indexOf(f.forgj));
      System.out.println(i + ", " + forgjString + ", " + f.dist);
    }
  }

  public void printTopologiskSortert() {
    Node l = topologisort();  // Henter den topologisk sorterte listen av noder.

    System.out.println("Topologisk sorterte noder:");
    while (l != null) {
      int nodeIndex = Arrays.asList(node).indexOf(l);
      System.out.println("Node " + nodeIndex);
      l = ((Topo_lst) l.d).neste;
    }
  }
}

class Kant {
  Kant neste;
  Node til;
  public Kant(Node n, Kant nst) {
    til = n;
    neste = nst;
  }
}

class Node {
  Kant kant1;
  Object d;
}

class Forgj {
  int dist;
  Node forgj;
  static int uendelig = 1000000000;
  public int finn_dist() { return dist; }
  public Node finn_forgj() { return forgj; }
  public Forgj() {
    dist = uendelig;
  }
}

class Topo_lst {
  boolean funnet;
  Node neste;
}

class Kø {
  private Object[] tab;
  private int start = 0;
  private int slutt = 0;
  private int antall = 0;
  public Kø(int str) {
    tab = new Object[str];
  }

  public boolean tom() {
    return antall == 0;
  }

  public boolean full() {
    return antall == tab.length;
  }

  public void leggIKø(Object e) {
    if(full()) return;
    tab[slutt] = e;
    slutt = (slutt + 1) % tab.length;
    ++antall;
  }

  public Object nesteIKø() {
    if (!tom()) {
      Object e = tab[start];
      start = (start + 1) % tab.length;
      --antall;
      return e;
    }
    else return null;
  }
}
