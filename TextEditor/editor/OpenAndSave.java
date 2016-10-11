package editor;

import java.io.*;
import java.util.List;
import java.util.LinkedList;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.VPos;
import javafx.scene.Group;


public class OpenAndSave {
    public static void open(Group root, File file, LinkedListD text) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            if (file.isDirectory()) {
                System.out.println("Unable to open file. "+ file + " is a directory.");
            }
            
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            int intRead = -1;
            while ((intRead = bufferedReader.read()) != -1) { 
                String charRead = String.valueOf((char) intRead);
                if (charRead.compareTo(Configuration.STRING_NEW_LINE) == 0) {
                    charRead = Configuration.STRING_ENTER;
                } 
                Text textRead = new Text(charRead);
                text.add(textRead);
                root.getChildren().add(textRead);
            }
            text.render(Configuration.windowWidth, Configuration.windowHeight, 
                Configuration.fontName, Configuration.fontSize);
            text.pointAtStart();
            Cursor.renderCursor();
            bufferedReader.close();
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.print("Unable to open file with name " + file);
            System.exit(1);
        } catch (IOException ioException) {
            System.out.print("Unable to create new file with name" + file);
            System.exit(1);
        }
    }

    public static void save(File file, LinkedListD text) {
        try {
            FileWriter writer = new FileWriter(file);
            text.pointAtStart();
            text.moveNext();
            while (!text.isAtStart()) {
                String charWrite = text.getItem().getText();
                writer.write(charWrite);
                text.moveNext();
            }
            writer.close();

        } catch (IOException ioException) {
            System.out.print("Unable to save file with name " + file);
            System.exit(1);
        }
    }



}


