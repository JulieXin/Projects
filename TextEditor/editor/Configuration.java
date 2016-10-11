package editor;


import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.Group;
import java.util.Stack;

public class Configuration {
    public static final String fontName = "Verdana";
    public static final int DEFAULT_FONT_SIZE = 12;
    public static final int DEFAULT_WINDOW_WIDTH = 500;
    public static final int DEFAULT_WINDOW_HEIGHT = 500;
    public static final int SCROLL_WIDTH = 20;
    public static final int TOP_MARGIN = 0;
    public static final int LEFT_MARGIN = 5;
    public static final int RIGHT_MARGIN = 5 + SCROLL_WIDTH;

    public static int fontSize = DEFAULT_FONT_SIZE;
    public static int windowWidth = DEFAULT_WINDOW_WIDTH;
    public static int windowHeight = DEFAULT_WINDOW_HEIGHT;

    public static final Text ENTER = new Text(Configuration.STRING_ENTER);
    public static final Text SPACE = new Text(" ");
    public static final String STRING_ENTER = "\r";
    public static final String STRING_NEW_LINE = "\n";

    public static File fileName;
    public static Stack<int[]> cursorMemory;
    public static Stack<Text> textMemory;
    public static Stack<int[]> cursorMemorySec;
    public static Stack<Text> textMemorySec;

    public static void handleWindowSize(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldScreenWidth, Number newScreenWidth) {
                windowWidth = newScreenWidth.intValue();
                LinkedListD.render(windowWidth, windowHeight, fontName, fontSize);
            	Cursor.renderCursor();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldScreenHeight, Number newScreenHeight) {
                windowHeight = newScreenHeight.intValue();
                LinkedListD.render(windowWidth, windowHeight, fontName, fontSize);
                Cursor.renderCursor();
            }
        });
    }

    public static void setFile(File file) {
        fileName = file;
    }

    public static void undoRedo() {
        while (cursorMemory.size() > 0) {
            int[] store = cursorMemory.pop();
            cursorMemorySec.add(store);
            LinkedListD.moveArrow(store[0], store[1]);
        }
    }
}

