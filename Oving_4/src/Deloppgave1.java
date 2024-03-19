public class Deloppgave1 {

  private class Node {
    int verdi;
    Node neste;

    public Node(int v, Node n) {
      verdi = v;
      neste = n;
    }

    public int finnElement() {
      return verdi;
    }

    public Node finnNeste() {
      return neste;
    }
  }

  private Node hode = null;
  private int antElementer = 0;

  public int finnAntall() {
    return antElementer;
  }

  public Node finnHode() {
    return hode;
  }

  public void settInnBakerst(int verdi) {
    Node nyNode = new Node(verdi, null);

    if (hode == null) {
      hode = nyNode;
      hode.neste = hode;  // her lager vi sirkelen ved å peke hode til seg selv
    } else {
      Node denne = hode;

      // Finner den siste noden, som i en sirkulær liste vil ha 'neste' pekende til hode.
      while (denne.neste != hode) {
        denne = denne.neste;
      }

      denne.neste = nyNode;
      nyNode.neste = hode; // her fullfører vi sirkelen ved å peke den nye noden tilbake til hode
    }
    ++antElementer;
  }

  public int josephusProblem(int n) {
    Node forrige = null;
    Node denne = hode;

    forrige = null;

    while (antElementer > 1) {
      // Hopper n-1 noder
      for (int i = 1; i < n; i++) {
        forrige = denne;
        denne = denne.neste;
      }

      // Fjerner n-te node ved å oppdatere pekerne
      forrige.neste = denne.neste;
      denne = denne.neste;
      antElementer--;
    }
    hode = denne;  // Setter hode til den gjenværende noden
    return denne.verdi;
  }

  public void printListe() {
    if (hode == null) return;
    Node denne = hode;
    do {
      System.out.print(denne.verdi + " ");
      denne = denne.neste;
    } while (denne != hode);
  }

  public int utførJosephus(int n, int k) {
    // Fyll listen med n mennesker
    for (int i = 1; i <= n; i++) {
      settInnBakerst(i);
    }

    // Returner resultatet av Josephus-problemet for k
    return josephusProblem(k);
  }

  public static void main(String[] args) {
    Deloppgave1 liste = new Deloppgave1();

    int n = 41; // Antall mennesker i sirkelen
    int m = 3; // Hver m-te person vil bli eliminert

    int sisteStående = liste.utførJosephus(n, m);

    System.out.print("Original liste: ");
    liste.printListe();
    System.out.println();
    System.out.println("Personen som står igjen etter Josephus-problemet med n=" + n + " og " + "m=" + m + " er: " + sisteStående);
  }
}