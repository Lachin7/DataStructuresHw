import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Compressor {

    public void compress(String inputName, String outputName, int v, int b) throws IOException {
        int W = (int) Math.pow(2,v);
        int B = (int) Math.pow(2,b);
        File file = new File(inputName);
        BufferedReader br  = new BufferedReader(new FileReader(file));;
        FileWriter fileWriter = new FileWriter(outputName);
        HashMap<Integer, LinkedList<Integer>> hashMap = new HashMap<>();
        for (int i = 0; i < 255; i++) {
            hashMap.put(i,new LinkedList<>());
        }
        getByte(v,b);
        fileWriter.write(((byte) getByte(v,b))+"");


            String line;
            while ((line = br.readLine()) != null) {
                fileWriter.write(compress(hashMap,line,v,b,W,B));

            }
            br.close();
    }

    private long getByte(int v, int b) {
       String s = intToBinary(v,5) + intToBinary(b,3);
       long a = 0;
        for (int i = 0; i < s.length(); i++) {
            a += (Math.pow(2,i) * Integer.parseInt( s.charAt(i) + ""));
        }
        return a;
    }


    private String compress(HashMap<Integer, LinkedList<Integer>> hashMap,String line, int v, int b, int W, int B) {
        int  i = 0, j=0;
        String reshte = "";
        String result = "";
        LinkedList linkedList = hashMap.get((int) line.charAt(0));
        linkedList.add(0);
        hashMap.put((int)line.charAt(j),linkedList);

        while (j+1<line.length()) {
            reshte = findLargestSubstring(hashMap, line, j, B);
            int a = reshte.length();
            if (a < (int) Math.ceil((v + b) / 8) + 1) {
                result += line.substring(j + 1, j + 2);
                j++;
                linkedList = hashMap.get((int) line.charAt(j));
         linkedList.add(j);
         hashMap.put((int)line.charAt(j),linkedList);


                if (j - i + 1 > W) {
                    linkedList = hashMap.get((int) line.charAt(i));
                    linkedList.remove();
                    hashMap.put((int)line.charAt(j),linkedList);
                    i++;

                }

            } else {
                int t = findLastStartingIndex(line, reshte, i, j) - i;

                result += "#" + (t * (2 * B) + a) + "#";

                for (int k = 0; k < a; k++) {
                    j++;
                    linkedList = hashMap.get((int) line.charAt(j));
                    linkedList.add(j);
                    hashMap.put((int)line.charAt(j),linkedList);
                }
                while (j - i + 1 > W) {
                    linkedList = hashMap.get((int) line.charAt(i));
                    linkedList.remove();
                    hashMap.put((int)line.charAt(j),linkedList);
                    i++;
                }

            }
        }
        return result;
    }

    private int findLastStartingIndex(String string, String reshte, int i, int j) {
        String s = string.substring(i,j+1);
        int index = s.indexOf(reshte);
        while (s.contains(reshte)){
            index = s.indexOf(reshte);
            s = s.substring(s.indexOf(reshte)+1);
        }
        return index;
    }


    private String findLargestSubstring(HashMap<Integer,LinkedList<Integer>> hashMap,String line,int j, int B){
        int l = Math.min(line.length(),B+j+1);
        LinkedList<Integer> indexes = hashMap.get((int)line.charAt(j+1));
        if (indexes.size()==0){
            return "";
        }
        else if (j+2 == l) return line.substring(j+1,j+2);
        int s = 1;
        for (int k = j + 2; k < l; k++) {
            if (indexes.size()==0){
                return line.substring(j+1,k-1);
            }
            LinkedList<Integer> list = hashMap.get((int) line.charAt(k));
            boolean b = false;
            for (int index : indexes) {
                for (int sec : list) {
                    if (index + s == sec) {
                        b = true;
                        break;
                    }
                }
               if (!b) indexes.remove();
            }
            s++;
        }

        return "";
    }

    public void decompress(String inputName, String outputName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(inputName));
        FileWriter fileWriter = new FileWriter(outputName);
        String line, all = "";
        while ((line = br.readLine()) != null) {
            all += line;
        }
        br.close();
         int b = line.getBytes()[0];
         int B = (int) Math.pow(2,b);

        int i = 0 , j= 0;
        for (int k = 0; k < all.length(); k++) {
            if (all.charAt(k) != '#'){
                fileWriter.write(all.charAt(k));
                j++;
                // to be completed
//                if (j-i> )
            }
            else {
                k++;
                String s = all.substring(k,all.indexOf('#',k));
                int t = (int) (Long.parseLong(s) / (2 * B));
                int a = (int) (Long.parseLong(s) % (2 * B));
                fileWriter.write(all.substring(t+i, a+t+i+1));
                 k += s.length();
                 j+= a ;

            }
        }

    }

    public static String intToBinary (int n, int numOfBits) {
        String binary = "";
        for(int i = 0; i < numOfBits; ++i, n/=2) {
            switch (n % 2) {
                case 0:
                    binary = "0" + binary;
                    break;
                case 1:
                    binary = "1" + binary;
                    break;
            }
        }
        return binary;
    }

//    public  void listFilesForFolder(final File folder) {
//        for (final File fileEntry : folder.listFiles()) {
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
//                compress(fileEntry);
//            }
//        }
//    }
//
//    private  void compressFolder(String fileName) {
//        final File folder = new File(fileName);
//        listFilesForFolder(folder);
//    }


//    private String compress(String line, int v, int b, int W, int B) {
//        int  i = 0, j=0;
//        String string = line;
//        String reshte = "";
//        String result = "";
//        HashMap<String, LinkedList<Integer>> hashMap = new HashMap<>();
//        reshte = findLargestSubstring(hashMap,string,i,j,B);
//        int a = reshte.length();
//        if (a < (int) Math.ceil((v+b)/8)+1){
//            result += string.substring(j+1,j+2);
//            j++;
//            for (int k = i; k < j+1; k++) {
//                String sub = string.substring(k,j+1);
//                LinkedList<Integer> linkedList = new LinkedList<>();
//                if (hashMap.containsKey(sub)) linkedList =hashMap.get(sub);
//                linkedList.add(k);
//                hashMap.put(sub,linkedList);
//            }
//
//            if (j-i+1>W){
//                for (int k = j; k < i+1 ; k--) {
//                    String sub = string.substring(i,k);
//                    LinkedList<Integer> linkedList =hashMap.get(sub);
//                    linkedList.remove(i);
//                    if (linkedList.size()==0) hashMap.remove(sub);
//                }
//                i++;
//
//            }
//
//        }
//        else {
//            int t = findLastStartingIndex(string,reshte,i,j );
//            //..
//
//            for (int k = 0; k < a; k++) {
//                j++;
//            }
//            while (j-i+1>W){
//                i--;
//            }
//
//        }
//        return result;
//    }

//    private String findLargestSubstring(HashMap<String,LinkedList<Integer>> hashMap,String string, int i, int j, int B){
////        String window = string.substring(i,j+1);
//        for (int k = string.length(); k > (j+1) ; k--) {
//            String reshte = string.substring(j+1,k);
//            if (reshte.length()<= B){
//                if (hashMap.containsKey(reshte))return reshte;
//
////                if (window.contains(reshte)) return reshte;
//            }
//        }
//        return "";
//    }

//    char [] chars =line.toCharArray();
//        for (int k = 0; k < chars.length; k++) {
//        LinkedList linkedList = hashMap.get((int)chars[k]);
//        linkedList.add(k);
//        hashMap.put((int)chars[k],linkedList);
//    }

}
