import javax.swing.*;
import java.awt.*;
import java.io.File;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("L-ZIP");
        frame.setSize(550,550);
        frame.setLocationRelativeTo(null);
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea("Enter your file name below");
        textArea.setSize(200,200);
        panel.add(textArea,BorderLayout.NORTH);
        JTextField textField = new JTextField();
        textField.setSize(200,100);
        panel.add(textField,BorderLayout.CENTER);
        JButton done = new JButton("done");
        done.setSize(100,100);
        done.addActionListener(e -> {
            if (textField.getText()!= null && !textField.getText().equals("")){
                try {
                    readFile(new File(textField.getText()));
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        panel.add(done,BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);




    }

    private static void readFile(File fileEntry) throws IOException {
        Scanner scanner = new Scanner(fileEntry);
        Compressor compressor = new Compressor();
        while (scanner.hasNext()){
            String command = scanner.nextLine();
            if (command.startsWith("UNZIP")){
                String inputName, outputName;
                if (command.charAt(6)=='\"') inputName = command.substring(7,command.indexOf("\"",8));
                else inputName = command.substring(6,command.indexOf(" ",6));
                command = command.substring(command.indexOf(inputName)+inputName.length());
                command = command.substring(command.indexOf(" ")+1);
                if (command.charAt(0)=='\"') outputName = command.substring(1,command.indexOf("\"",2));
                else outputName = command;
                compressor.decompress(inputName,outputName);
            }
            else if (command.startsWith("ZIP")){
                String inputName, outputName;
                int v, b;
                if (command.charAt(4)=='\"') inputName = command.substring(5,command.indexOf("\"",6));
                else inputName = command.substring(4,command.indexOf(" ",4));
                command = command.substring(command.indexOf(inputName)+inputName.length());
                command = command.substring(command.indexOf(" ")+1);
                if (command.charAt(0)=='\"') outputName = command.substring(1,command.indexOf("\"",2));
                else outputName = command.substring(0,command.indexOf(" ",0));
                command = command.substring(command.indexOf(outputName)+outputName.length());
                command = command.substring(command.indexOf(" ")+1);
                v = Integer.parseInt(command.substring(0,command.indexOf(" ")));
                b = Integer.parseInt(command.substring(command.indexOf(" ")+1));
                compressor.compress(inputName,outputName,v,b);
            }
        }
    }



}
