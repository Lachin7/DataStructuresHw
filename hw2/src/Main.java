import java.util.Scanner;
import java.util.Stack;

public class Main {
    static class Node {
        public String value;
        public Node leftChild,rightChild;

        public Node(String x) {
            value = x;
        }

        public void printValue() {
            if (value.equals("_")) System.out.print("-" + " ");
            else System.out.print(value + " ");
        }

        public boolean isOperator() {
            return value.equals("+") || value.equals("-") ||value.equals("*") ||value.equals("/") ||value.equals("^") || value.equals("_");
        }
    }
    static class Conversion {
        private String inputString, outputString = "";
        private Stack<Character> stack = new Stack<>();

        public Conversion(String input) {
            this.inputString = input;
        }

        public String infixToPostfix() {
            for (int i = 0; i < inputString.length(); i++) {
                char ch = inputString.charAt(i);
                if (Character.isDigit(ch) || ch=='.') outputString += ch;
                else {
                    outputString += " ";
                    if (ch == '(') stack.push(ch);
                    else if (ch == ')') getMatchingParenthesis();
                    else if (isOperator(ch)) {
                        if (ch=='-' && isUnary(i))ch='_';
                        while (!stack.isEmpty() && action(ch,stack.peek())) {
                            outputString = outputString + "  "+stack.pop() + "  ";
                        }
                        stack.push(ch);
                    }
                }

            }
            while (!stack.isEmpty()){
                char ch = stack.pop();
                if (isOperator(ch))outputString = outputString + " "+ ch + " ";
                else  outputString += ch;
            }
            System.err.println(outputString);
            return outputString;
        }

        private boolean action(char ch, char peek) {
            if (ch=='_' && peek=='_')return true;
            if (ch=='^' && peek=='_')return true;
            if (ch=='/'){
                if (peek=='*' || peek=='/' || peek=='^' || peek=='_' )return true;
            }
            if (ch=='*'){
                if (peek=='*' || peek=='/' || peek=='^' || peek=='_' )return true;
            }
            if (ch=='+' && !(peek=='('))return true;
            if (ch=='-' && !(peek=='('))return true;

            return false;

        }

        private boolean isUnary( int i) {
            return   i == 0 || isOperator(inputString.charAt(i-1)) || inputString.charAt(i-1)=='(';
        }


        private boolean isOperator(char ch) {
            return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '_';
        }

        static int precedence(char ch) {
            switch (ch)
            {
                case '_':
                    return 4;
                case '+':
                case '-':
                    return 1;

                case '*':
                case '/':
                    return 2;

                case '^':
                    return 3;
            }
            return -1;
        }


        private void getMatchingParenthesis() {
            while (!stack.isEmpty() && stack.peek() != '(')
                outputString += " "+stack.pop() +" ";
            stack.pop();
        }
    }

    static class Tree {
        private Node root;

        public Tree() {
            root = null;
        }

        private double calculate(Node node){
            if (node.rightChild==null && node.leftChild==null && !(node.value.equals("+") || node.value.equals("-") ||node.value.equals("*") ||node.value.equals("/") ||node.value.equals("^") || node.value.equals("_") )) return (Double.valueOf(node.value));
            double leftVal = 0,rightVal=0;
            if (node.leftChild!=null) leftVal = calculate(node.leftChild);

            if (node.rightChild!=null) rightVal = calculate(node.rightChild);

            if (node.value.equals("-")) return  (leftVal - rightVal);

            else if (node.value.equals("+")) return (leftVal + rightVal);

            else if (node.value.equals("*")) return  (leftVal * rightVal);
            else if (node.value.equals("^")) return  Math.pow(leftVal,rightVal);

            else if (node.value.equals("_")) return -rightVal;

            else return  (leftVal / rightVal);

        }

        public void construct(String string) {
            Conversion conversion = new Conversion(string);
            string = conversion.infixToPostfix();
            Stack<Node> stack = new Stack<>();
            String[] array = string.split("\\s+");
            for (int i = 0; i < array.length; i++) {
                String data = array[i];
                Node newNode;
                if (data.equals("+") || data.equals("-") ||data.equals("*") ||data.equals("/") ||data.equals("^")  ){
                    newNode = new Node(data);
                    Node node = stack.pop();
                    Node node1 = stack.pop();
                    newNode.rightChild = node;
                    newNode.leftChild = node1;
                    stack.push(newNode);
                }
                else if (data.equals("_")){
                    newNode = new Node(data);
                    Node node1 = stack.pop();
                    newNode.rightChild = node1;
                    stack.push(newNode);
                }
                else if (!data.equals("")&& !data.equals(" ")){
                    newNode = new Node(data);
                    stack.push(newNode);
                }
            }
            root = stack.pop();

        }

        public void preOrder(Node node) {
            if (node != null) {
                node.printValue();
                preOrder(node.leftChild);
                preOrder(node.rightChild);
            }
        }

        public void postOrder(Node node) {
            if (node != null) {
                postOrder(node.leftChild);
                postOrder(node.rightChild);
                node.printValue();
            }
        }

        public String inOrder(Node node) {
            if (node != null) {
                if (node.value.equals("+") || node.value.equals("-") || node.value.equals("*") || node.value.equals("/") || node.value.equals("^")) {
                    return("( " + inOrder(node.leftChild) + " " + node.value + " " + inOrder(node.rightChild) + " )");
//                    if (node.leftChild.isOperator() && !(node.rightChild.isOperator())) return("( " + inOrder(node.leftChild) + " ) " + node.value + " " + inOrder(node.rightChild));
//                    else if (node.rightChild.isOperator() &&!(node.leftChild.isOperator())) return( inOrder(node.leftChild) + " " + node.value + " ( " + inOrder(node.rightChild) + " )");
//                    else if (!(node.rightChild.isOperator()) &&!(node.leftChild.isOperator()))return("( " + inOrder(node.leftChild) + " " + node.value + " " + inOrder(node.rightChild) + " )");
//                    else if ((node.rightChild.isOperator()) && (node.leftChild.isOperator()))return("( " + inOrder(node.leftChild) + " ) " + node.value + " ( " + inOrder(node.rightChild) + " )");

                }
                else if (node.value.equals("_")) return   "( " + "- " + inOrder(node.rightChild) + " )" ;
                else return node.value;
            }
            return "";
        }

        public Node getRoot() {
            return root;
        }

    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Tree tree = new Tree();
        tree.construct(input);
        System.err.println(tree.inOrder(tree.getRoot()));
        System.out.printf("%.2f",tree.calculate(tree.getRoot()));
        System.out.println();
        tree.preOrder(tree.getRoot());
        System.out.println();
        System.out.println(tree.inOrder(tree.getRoot()));
        tree.postOrder(tree.getRoot());
    }

}

