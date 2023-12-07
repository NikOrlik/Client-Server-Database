package Create_file;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CreateFile {

    public static void createF(String text)
    {
        JFileChooser fileChooser= new JFileChooser();
        fileChooser.setCurrentDirectory(new File("."));
        int response= fileChooser.showSaveDialog(null);
        if(response==JFileChooser.APPROVE_OPTION)
        {
            File file= new File(fileChooser.getSelectedFile().getAbsolutePath());
            PrintWriter fileout=null;
            try {
                fileout= new PrintWriter(file);
                fileout.println(text);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            finally {
                fileout.close();
            }
        }
    }}
