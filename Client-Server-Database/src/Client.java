import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.*;
import Create_file.CreateFile;
import  Create_file.*;

public class Client{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    JFrame console;
    static JTextArea area;
    JButton enterLabel;JMenuBar menuBar;JMenu settings; JMenuItem changeFColor;JMenuItem changeBColor;JMenu func;JMenuItem file;Image image;
    JSpinner   fontSizeSpinner;
    boolean closeThread =false;
    private String inputLine;
    String bufferString="";

    public Client() throws IOException {

        console= new JFrame("Library");
        console.setSize(600,600);
        area = new JTextArea();
        area.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(area);
        console.add(scrollPane, BorderLayout.CENTER);
        image = new ImageIcon("library.png").getImage();
        console.setIconImage(image);

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setValue(12);

        enterLabel = new JButton("Enter");
        console.add(enterLabel, BorderLayout.SOUTH);
        menuBar= new JMenuBar();
        settings= new JMenu("Settings");
        func = new JMenu("Functions");

        changeFColor = new JMenuItem("Font Color");
        changeBColor = new JMenuItem("Back Color");
        file=new JMenuItem("Save data from app");

        //set "colors"---------------------
        area.setBackground(Color.BLACK);
        area.setForeground(Color.GREEN);
        area.setCaretColor(Color.GREEN);
        //--------------------------------------------------
        //add all components
        settings.add(changeBColor);
        settings.add(changeFColor);

        func.add(file);


        menuBar.add(settings);
        menuBar.add(func);
        console.add(menuBar,BorderLayout.NORTH);
        console.add(fontSizeSpinner,BorderLayout.LINE_START);
        //-------------------------------

        //variables for communicating with server
        socket = new Socket("localhost", 1111);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


        //---------------------------------------------------------------

        //Anonymous Listeners
        changeFColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser= new JColorChooser();
                Color color= colorChooser.showDialog(null, "Choose a color", Color.BLACK);

                area.setForeground(color);
            }
        });
        changeBColor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JColorChooser colorChooser= new JColorChooser();
                Color color= colorChooser.showDialog(null, "Choose a color", Color.BLACK);

                area.setBackground(color);
            }
        });
        fontSizeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                area.setFont(new Font(area.getFont().getFamily(),Font.PLAIN ,(int)fontSizeSpinner.getValue()));
            }

        });
        file.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateFile.createF(area.getText());
            }
        });
        console.setVisible(true);
    }

    public void readFromServer() {
        new Thread(() -> {
            String inputLine;
            try {
                while ((inputLine = in.readLine()) != null) {
                    if(inputLine.equals("Connection lost")) {
                        area.setText(inputLine);
                        closeThread =true;
                        console.dispose();
                        return;//"close" thread
                    }


                    area.append(inputLine+"\n->");
                    bufferString = inputLine;
                    area.setCaretPosition(area.getDocument().getLength());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    public void writeToServer() {

        area.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    e.consume();
                    String[] lines = area.getText().split("->");
                    String lastLine = lines[lines.length - 1];
                    area.setText("");
                    if (lastLine == null || lastLine.trim().isBlank() || (bufferString!=null && !bufferString.trim().isEmpty() && lastLine.contains(bufferString))) {
                        out.println("");area.setText("");
                        System.out.println(bufferString);
                    } else {
                        out.println(lastLine);area.setText("");
                    }

                }
            }

        });
        enterLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] lines = area.getText().split("->");
                String lastLine = lines[lines.length - 1];
                area.setText("");
                if (lastLine == null || lastLine.trim().isBlank() || (bufferString!=null && !bufferString.trim().isEmpty() && lastLine.contains(bufferString))) {
                    out.println("");area.setText("");
                    System.out.println(bufferString);
                } else {
                    out.println(lastLine);area.setText("");
                }
            }});

    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.readFromServer();client.writeToServer();
    }
}
