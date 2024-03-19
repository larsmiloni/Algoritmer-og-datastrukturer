public class Token {
  // Literal or (Length, Distance) pair
  final boolean isLiteral;
  final char literal;
  final int distance, length;

  // Constructor for literals
  public Token(char literal) {
    this.isLiteral = true;
    this.literal = literal;
    this.distance = this.length = 0;
  }

  // Constructor for length/distance pairs
  public Token(int length, int distance) {
    this.isLiteral = false;
    this.literal = 0;
    this.length = length;
    this.distance = distance;
  }

  @Override
  public String toString() {
    if (isLiteral) {
      return String.valueOf(literal);
    } else {
      return "(" + length + "," + distance + ")";
    }
  }
}
