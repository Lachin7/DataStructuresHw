import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MyList<String> subjects = new MyList<>(), persons = new MyList<>() ;
        MyMatrix interestMatrix = new MyMatrix(),peopleRelation = new MyMatrix();;

        int t = scanner.nextInt();
        scanner.nextLine();
        if (t>1) {
            for (int i = 0; i < t; i++) {
                int m = scanner.nextInt();
                MyList<String> localSubjects = new MyList<>(),localPersons = new MyList<>();;
                for (int j = 0; j < m; j++) {
                    String subject = scanner.next();
                    localSubjects.insert(new Node(j, subject));
                    if (!subjects.contains(subject)) subjects.insert(new Node(subjects.getSize(), subject));
                }

                scanner.nextLine();
                int n = scanner.nextInt();
                for (int j = 0; j < n; j++) {
                    String person = scanner.next();
                    localPersons.insert(person);
                    if (!persons.contains(person)) persons.insert(person);
                    int r = scanner.nextInt();
                    for (int k = 0; k < r; k++) {
                        String relation = scanner.next();
                        int subjectIndex = Integer.parseInt(relation.substring(0, relation.indexOf(":")));
                        double rate = Double.parseDouble(relation.substring(relation.indexOf(":") + 1));
                        int number = subjects.getIndex(localSubjects.getValue(subjectIndex));

                        if (interestMatrix.rows.getNode(persons.getIndex(person)) == null) {
                            MyList<Double> rowList = new MyList<>();
                            rowList.insert(new Node(number, rate));
                            interestMatrix.insertRow(persons.getIndex(person), rowList);
                        } else {
                            MyList<Double> rowList = interestMatrix.rows.getValue(persons.getIndex(person));
                            if (rowList.getNode(number) != null)
                                rowList.getNode(number).setValue(rowList.getValue(rowList.getNode(number)) + rate);
                            else rowList.insert(new Node(number, rate));
                        }
                    }
                }

                scanner.nextLine();
                int d = scanner.nextInt();
                for (int j = 0; j < d; j++) {
                    int person = scanner.nextInt();
                    int num = scanner.nextInt();
                    person = persons.getIndex(localPersons.getValue(person));
                    for (int k = 0; k < num; k++) {
                        String relation = scanner.next();
                        int friendIndex = Integer.parseInt(relation.substring(0, relation.indexOf(":")));
                        double rate = Double.parseDouble(relation.substring(relation.indexOf(":") + 1));
                        int friend = persons.getIndex(localPersons.getValue(friendIndex));
                        if (peopleRelation.rows.getNode(person) == null) {
                            MyList<Double> rowList = new MyList<>();
                            rowList.insert(new Node(friend, rate));
                            peopleRelation.insertRow(person, rowList);
                        } else {
                            MyList<Double> rowList = peopleRelation.rows.getValue(peopleRelation.rows.getNode(person));
                            if (rowList.getNode(friend) != null) {
                                rowList.getNode(friend).setValue(rowList.getValue(rowList.getNode(friend)) + rate);
                            } else rowList.insert(new Node(friend, rate));
                        }
                    }
                    scanner.nextLine();
                }
            }
        }
        else {
            for (int i = 0; i < t; i++) {
                int m = scanner.nextInt();
                for (int j = 0; j < m; j++) {
                    String subject = scanner.next();
                    subjects.insert(new Node(subjects.getSize(), subject));
                }
                scanner.nextLine();
                int n = scanner.nextInt();
                for (int j = 0; j < n; j++) {
                    String person = scanner.next();
                    persons.insert(person);
                    int r = scanner.nextInt();
                    MyList<Double> rowList = new MyList<>();
                    for (int k = 0; k < r; k++) {
                        String relation = scanner.next();
                        int number = Integer.parseInt(relation.substring(0, relation.indexOf(":")));
                        double rate = Double.parseDouble(relation.substring(relation.indexOf(":") + 1));
                        rowList.insert(new Node(number, rate));
                    }
                    interestMatrix.insertRow(j, rowList);
                }

                scanner.nextLine();
                int d = scanner.nextInt();
                for (int j = 0; j < d; j++) {
                    int person = scanner.nextInt();
                    int num = scanner.nextInt();
                    MyList<Double> rowList = new MyList<>();
                    for (int k = 0; k < num; k++) {
                        String relation = scanner.next();
                        int friend = Integer.parseInt(relation.substring(0, relation.indexOf(":")));
                        double rate = Double.parseDouble(relation.substring(relation.indexOf(":") + 1));
                        rowList.insert(new Node(friend, rate));
                    }
                    peopleRelation.insertRow(person, rowList);
                    scanner.nextLine();
                }
            }
        }
        int q = scanner.nextInt();
        scanner.nextLine();
        interestMatrix.sort();
        peopleRelation.sort();
        for (int i = 0; i < q; i++) {
            int q_i = scanner.nextInt();
            MyList<Integer> querySubjects = new MyList();
            for (int j = 0; j < q_i; j++) {
                String subject = scanner.next();
                querySubjects.insert(subjects.getIndex(subject));
            }
            int omq = scanner.nextInt();
            calculate(interestMatrix, peopleRelation, querySubjects, persons, omq);
            scanner.nextLine();
        }
    }

    private static void calculate(MyMatrix interestMatrix,MyMatrix relations, MyList<Integer> querySubjects, MyList<String> persons, int omq) {

        MyList<Double> newInterest = new MyList();
        Node currentNode = interestMatrix.rows.head;
        while (currentNode!=null){
            MyList currentRow = (MyList) currentNode.value;

            Node currentNodeInQuery = querySubjects.head;
            double value = 0;
            while (currentNodeInQuery!=null){
                if (currentRow.getNode((Integer) currentNodeInQuery.value) == null){
                    value=0;
                    break;
                }
                double v = (double) currentRow.getNode((Integer) currentNodeInQuery.value).getValue();
//                if (v==0) {
//                    value = 0;
//                    break;
//                }
                value += v;
                currentNodeInQuery = currentNodeInQuery.next;
            }
            if (value != 0)newInterest.insert(new Node(currentNode.number,value));
            currentNode = currentNode.next;
        }

        MyList<Integer> names = new MyList<>();
        if (omq>=0){
            OutPutList<Double,String> out = makeOutPut(persons, newInterest);
            OutPutNode<Double,String> current = out.head;
            while (current!=null){
                if (current.value>0){
                    System.out.print(persons.getNode(current.number).getValue()+" ");
                    System.out.printf("%.6f", current.value);
                    System.out.println(" ");
                    names.insert(current.number);
                }
                current = current.next;
            }
        }
        if (omq>=1){
            newInterest = multiply(relations,newInterest);
            OutPutList<Double,String> out = makeOutPut(persons, newInterest);
            OutPutNode<Double,String> current = out.head;
            while (current!=null){
                if (current.value>0 && !names.contains(current.number)){
                    System.out.print(persons.getNode(current.number).getValue()+" ");
                    System.out.printf("%.6f", current.value);
                    System.out.println(" +");
                    names.insert(current.number);
                }
                current = current.next;
            }
        }
        if (omq>=2){
            newInterest = multiply(relations,newInterest);
            OutPutList<Double,String> out = makeOutPut(persons, newInterest);
            OutPutNode<Double,String> current = out.head;
            while (current!=null){
                if (current.value>0 && !names.contains(current.number)) {
                    System.out.print(persons.getNode(current.number).getValue()+" ");
                    System.out.printf("%.6f", current.value);
                    System.out.println(" ++");
                    names.insert(current.number);
                }
                current = current.next;
            }
        }
        if (omq>=3){
            newInterest = multiply(relations,newInterest);
            OutPutList<Double,String> out = makeOutPut(persons, newInterest);
            OutPutNode<Double,String> current = out.head;
            while (current!=null){
                if (current.value>0 && !names.contains(current.number)) {
                    System.out.print(persons.getNode(current.number).getValue() + " ");
                    System.out.printf("%.6f", current.value);
                    System.out.println(" +++");
                    names.insert(current.number);
                }
                current = current.next;
            }
        }
    }

    private static OutPutList<Double, String> makeOutPut(MyList persons ,MyList<Double> newInterest) {
        Node current = newInterest.head;
        OutPutList<Double,String> list = new OutPutList<>();
        while (current!=null){
            list.insert(new OutPutNode(current.value,persons.getNode(current.number).getValue(),current.number));
            current = current.next;
        }
        list.head = list.mergeSort(list.head);
        return list;
    }

    /** MyMatrix class*/

    public static class MyMatrix {

        private MyList<MyList<Double>> rows;

        public MyMatrix() {
            rows = new MyList<>();
        }

        public void insertRow(int number,MyList<Double> list){
            rows.insert(new Node(number,list));
        }

        @Override
        public String toString() {
            return "MyMatrix{" +
                    ", rows=" + rows +
                    '}';
        }

        public int getRowNum(){
            return rows.getSize();
        }

        public void sort() {
            rows.head = rows.mergeSort(rows.head, true);
            Node currentNode = rows.head;
            while (currentNode!=null){
                MyList currentRow = (MyList) currentNode.value;
                currentRow.head = currentRow.mergeSort(currentRow.head, true);
                currentNode = currentNode.next;
            }
        }
    }

    /** MyMatrix methods*/

    static MyList<Double> multiply(MyMatrix matrix, MyList<Double> list){
        MyList<Double> result = new MyList<>();
        list.head = list.mergeSort(list.head,true);
        Node<MyList<Double>> currentNode = matrix.rows.head;
        while (currentNode!=null){
            MyList<Double> currentRow = currentNode.value;
            Node<Double> node1 = currentRow.head;
            Node<Double> node2 = list.head;
            double value = 0;
            while (node1!=null && node2!=null){
                if (node1.number == node2.number){
                    value += node1.getValue()*node2.getValue();
                    node1 = node1.next;
                    node2 = node2.next;
                }
                else if (node1.number < node2.number) node1 = node1.next;
                else node2 = node2.next;
            }
            if (value > 0)result.insert(new Node(currentNode.number,value));
            currentNode = currentNode.next;
        }
        return result;
    }

    /** MyList class*/

    public static class MyList<T> {

        private Node<T> head;

        public void insert(T value){
            insert(new Node(getSize(),value));
        }

        public void insert(Node node){
            if(this.head == null) head = node;
            else {
                Node currentNode = head;
                while(currentNode.getNext() != null) currentNode = currentNode.getNext();
                currentNode.setNext(node);
            }
        }

        public boolean isEmpty(){
            return head==null;
        }

        public int getSize(){
            if (isEmpty())return 0;
            Node current = head;
            int size = 1;
            while (current.getNext()!=null){
                size++;
                current = current.getNext();
            }
            return size;
        }

        public boolean contains(T value){
            Node current = head;
            while (current != null) {
                if (current.getValue().equals(value)) return true;
                current = current.getNext();
            }
            return false;
        }

        Node mergeSort(Node<Double> a, Node<Double> b, boolean number) {
            Node result = null;
            if (a == null) return b;
            else if (b == null) return a;
            else if(number) {
                if (a.number <= b.number) {
                    result = a;
                    result.next = mergeSort(a.next, b, true);
                } else {
                    result = b;
                    result.next = mergeSort(a, b.next, true);
                }
            }
            else {
                if (a.value - b.value >=0) {
                    result = a;
                    result.next = mergeSort(a.next, b, false);
                } else {
                    result = b;
                    result.next = mergeSort(a, b.next, false);
                }
            }
            return result;
        }

        Node mergeSort(Node head, boolean number) {
            if (head == null || head.next == null) return head;
            Node middle = getMiddle(head);
            Node nexToMiddle = middle.next;
            middle.next = null;
            Node list1 = mergeSort(head, number);
            Node list2 = mergeSort(nexToMiddle,number);
            Node sortedlist = mergeSort(list1, list2, number);
            return sortedlist;
        }

        public static Node getMiddle(Node head) {
            if (head == null) return null;
            Node slow = head, fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }


        public boolean contains(Node node){
            Node current = head;
            while (current != null) {
                if (current==node) return true;
                current = current.getNext();
            }
            return false;
        }


        public int getIndex(T value){
            Node current = head;
            if (!contains(value)) return -1;
            int index = 0;
            while (current != null) {
                if (current.getValue().equals(value)) return index ;
                current = current.getNext();
                index ++;
            }
            return index;
        }

        public T getValue(int index){
            if (index<0 || index>= getSize())return null;
            Node current = head;
            for (int i = 0; i < index; i++) current = current.getNext();
            return (T) current.getValue();
        }

        public T getValue(Node node){
            return (T) node.getValue();
        }

        public Node getNode(int number){
            Node current = head;
            while (current!=null) {
                if (current.number==number) return current;
                current = current.next;
            }
            return null;
        }


        @Override
        public String toString() {
            String list = "My List = { ";
            Node current = head;
            for (int i = 0; i < getSize(); i++){
                list += current.toString() ;
                if (i!=getSize()-1) list += ", ";
                current = current.getNext();
            }
            return list + " }";
        }

    }

    public static class Node<T> {

        private int number;
        private T value;
        private Node next;

        public Node(T value){
            this.value = value;
            this.next = null;
        }

        public Node(int number, T value) {
            this.number = number;
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "number=" + this.number +
                    ", value=" + this.value +
                    '}';
        }
    }

    public static class OutPutList<T,S>{
        private OutPutNode<T,S> head;

        public void insert(OutPutNode node){
            if(this.head == null) head = node;
            else {
                OutPutNode currentNode = head;
                while(currentNode.next != null) currentNode = currentNode.next;
                currentNode.next = (node);
            }
        }

        public boolean isEmpty(){
            return head==null;
        }

        public int getSize(){
            if (isEmpty())return 0;
            OutPutNode current = head;
            int size = 1;
            while (current.next!=null){
                size++;
                current = current.next;
            }
            return size;
        }

        OutPutNode mergeSort(OutPutNode<Double,String> a, OutPutNode<Double,String> b) {
            OutPutNode result = null;
            if (a == null) return b;
            else if (b == null) return a;
            if (a.value - b.value>0) {
                result = a;
                result.next = mergeSort(a.next, b);
            } else if ( a.value - b.value == 0){
                if (a.name.compareTo(b.name)<= 0){
                    result = a;
                    result.next = mergeSort(a.next, b);
                }
                else {
                    result = b;
                    result.next = mergeSort(a, b.next);
                }
            }else {
                result = b;
                result.next = mergeSort(a, b.next);
            }

            return result;
        }

        OutPutNode mergeSort(OutPutNode head) {
            if (head == null || head.next == null) return head;
            OutPutNode middle = getMiddle(head);
            OutPutNode nexToMiddle = middle.next;
            middle.next = null;
            OutPutNode list1 = mergeSort(head);
            OutPutNode list2 = mergeSort(nexToMiddle);
            OutPutNode sortedlist = mergeSort(list1, list2);
            return sortedlist;
        }

        public static OutPutNode getMiddle(OutPutNode head) {
            if (head == null) return null;
            OutPutNode slow = head, fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            return slow;
        }
    }

    public static class OutPutNode<T,S> {

        private T value;
        private S name ;
        private int number;
        private OutPutNode next;

        public OutPutNode(T value, S name,int number) {
            this.value = value;
            this.name = name;
            this.number = number;
        }
    }

}


