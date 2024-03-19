public class Deloppgave3 {

  static class Node {
    String value;
    Node left, right;

    Node(String item) {
      value = item;
      left = right = null;
    }
  }

  public static int evaluate(Node root) {
    if (root == null) {
      return 0;
    }

    if (root.left == null && root.right == null) {
      return Integer.parseInt(root.value);
    }

    int leftVal = evaluate(root.left);
    int rightVal = evaluate(root.right);

    switch (root.value) {
      case "+":
        return leftVal + rightVal;
      case "-":
        return leftVal - rightVal;
      case "*":
        return leftVal * rightVal;
      case "/":
        if(rightVal == 0) {
          throw new ArithmeticException("Division by Zero");
        }
        return leftVal / rightVal;
    }
    return 0; // This line shouldn't be reached, just a default return.
  }

  public static String formula(Node root) {
    if (root == null) {
      return "";
    }

    if (root.left == null && root.right == null) {
      return root.value;
    }

    String leftExpression = formula(root.left);
    String rightExpression = formula(root.right);

    return "(" + leftExpression + " " + root.value + " " + rightExpression + ")";
  }

  public static void main(String[] args) {
    Node root = new Node("/");

    root.left = new Node("*");
    root.left.left = new Node("3");
    root.left.right = new Node("+");
    root.left.right.left = new Node("2");
    root.left.right.right = new Node("4");

    root.right = new Node("-");
    root.right.left = new Node("7");
    root.right.right = new Node("*");
    root.right.right.left = new Node("2");
    root.right.right.right = new Node("2");

    System.out.println(formula(root) + " = " + evaluate(root)); // This should print 830 because 30 + 20*40 = 830
  }
}
